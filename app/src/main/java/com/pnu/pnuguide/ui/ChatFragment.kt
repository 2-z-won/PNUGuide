package com.pnu.pnuguide.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pnu.pnuguide.R
import kotlinx.coroutines.flow.collectLatest

class ChatFragment : Fragment() {

    private val viewModel by viewModels<ChatViewModel> {
        AndroidViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatAdapter()
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_chat)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val edit = view.findViewById<EditText>(R.id.edit_message)
        view.findViewById<Button>(R.id.button_send).setOnClickListener {
            val text = edit.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(text)
                edit.setText("")
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messages.collectLatest { adapter.submitList(it) }
        }
    }
}
