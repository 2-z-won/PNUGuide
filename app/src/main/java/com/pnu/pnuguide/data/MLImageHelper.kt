package com.pnu.pnuguide.data

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.Interpreter

object MLImageHelper {
    private var model: Any? = null
    private var labels: List<String>? = null
    private var embeddings: Array<FloatArray>? = null

    fun bitmapToTensorImage(bitmap: Bitmap): TensorImage {
        val processor = ImageProcessor.Builder()
            // The TFLite model expects 64x64 RGB images. Using a larger size
            // causes a shape mismatch when copying the buffer into the tensor.
            .add(ResizeOp(64, 64, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()
        val tensor = TensorImage(DataType.FLOAT32)
        tensor.load(bitmap)
        return processor.process(tensor)
    }

    fun bitmapToTensorBuffer(bitmap: Bitmap): TensorBuffer {
        return bitmapToTensorImage(bitmap).tensorBuffer
    }

    private fun ensureLoaded(context: Context) {
        if (model == null) {
            model = ModelLoader.loadModel(context)
        }
        if (labels == null) {
            labels = ModelLoader.loadLabels(context)
        }
        if (embeddings == null) {
            embeddings = loadEmbeddings(context)
        }
    }

    private fun loadEmbeddings(context: Context): Array<FloatArray> {
        context.assets.open("embeddings.npy").use { input ->
            val magic = ByteArray(6)
            input.read(magic)
            input.skip(2) // version
            val headerLenBytes = ByteArray(2)
            input.read(headerLenBytes)
            val headerLen = java.nio.ByteBuffer.wrap(headerLenBytes)
                .order(java.nio.ByteOrder.LITTLE_ENDIAN).short.toInt()
            val headerBytes = ByteArray(headerLen)
            input.read(headerBytes)
            val header = String(headerBytes)
            val shapeMatch = Regex("\\((\\d+),\\s*(\\d+)\\)").find(header)
            val rows = shapeMatch?.groupValues?.getOrNull(1)?.toInt() ?: 0
            val cols = shapeMatch?.groupValues?.getOrNull(2)?.toInt() ?: 0
            val floats = ByteArray(rows * cols * 4)
            input.read(floats)
            val bb = java.nio.ByteBuffer.wrap(floats)
                .order(java.nio.ByteOrder.LITTLE_ENDIAN)
            return Array(rows) { r -> FloatArray(cols) { bb.float } }
        }
    }

    private fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        var dot = 0f
        var normA = 0f
        var normB = 0f
        for (i in a.indices) {
            dot += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        return if (normA == 0f || normB == 0f) 0f
        else (dot / kotlin.math.sqrt(normA * normB))
    }

    private fun runTflite(interpreter: Interpreter, bitmap: Bitmap): FloatArray {
        val input = bitmapToTensorBuffer(bitmap)
        val output = Array(1) { FloatArray(1280) }
        interpreter.run(input.buffer, output)
        return output[0]
    }

    private fun runPyTorch(module: Any, bitmap: Bitmap): FloatArray {
        return try {
            val tensorClass = Class.forName("org.pytorch.Tensor")
            val fromBlob = tensorClass.getMethod(
                "fromBlob",
                FloatArray::class.java,
                LongArray::class.java
            )
            val chw = bitmapToCHWArray(bitmap)
            // PyTorch model (if present) should use the same 64x64 input size
            // as the TensorFlow Lite model.
            val tensor = fromBlob.invoke(null, chw, longArrayOf(1, 3, 64, 64))

            val ivalueClass = Class.forName("org.pytorch.IValue")
            val fromTensor = ivalueClass.getMethod("from", tensorClass)
            val inputVal = fromTensor.invoke(null, tensor)

            val moduleClass = Class.forName("org.pytorch.Module")
            val forward = moduleClass.getMethod("forward", ivalueClass)
            val outValue = forward.invoke(module, inputVal)

            val toTensor = ivalueClass.getMethod("toTensor")
            val outTensor = toTensor.invoke(outValue)

            val dataMethod = tensorClass.getMethod("getDataAsFloatArray")
            dataMethod.invoke(outTensor) as FloatArray
        } catch (e: Exception) {
            throw IllegalStateException("PyTorch inference failed", e)
        }
    }

    private fun bitmapToCHWArray(bitmap: Bitmap): FloatArray {
        val scaled = Bitmap.createScaledBitmap(bitmap, 64, 64, true)
        val result = FloatArray(3 * 64 * 64)
        var rIdx = 0
        var gIdx = 64 * 64
        var bIdx = 2 * 64 * 64
        for (y in 0 until 64) {
            for (x in 0 until 64) {
                val pixel = scaled.getPixel(x, y)
                result[rIdx++] = ((pixel shr 16) and 0xFF) / 255f
                result[gIdx++] = ((pixel shr 8) and 0xFF) / 255f
                result[bIdx++] = (pixel and 0xFF) / 255f
            }
        }
        return result
    }

    suspend fun matchSpot(context: Context, bitmap: Bitmap): String? {
        // For compatibility with existing call sites, forward to classifyImage
        // which uses the TensorFlow Lite model to directly predict the label.
        return classifyImage(context, bitmap)
    }

    /**
     * Run classification on the given [bitmap] and return the label with the
     * highest probability.
     *
     * This uses the same model and label list loaded by [ensureLoaded]. It
     * expects the underlying model to directly output probabilities for each
     * label.
     */
    suspend fun classifyImage(context: Context, bitmap: Bitmap): String? {
        ensureLoaded(context)
        val interpreter = model as? Interpreter ?: return null
        val lbls = labels ?: return null

        val input = bitmapToTensorBuffer(bitmap)
        val output = Array(1) { FloatArray(lbls.size) }
        interpreter.run(input.buffer, output)
        val probs = output[0]
        val bestIdx = probs.indices.maxByOrNull { probs[it] } ?: return null
        return lbls.getOrNull(bestIdx)
    }
}

