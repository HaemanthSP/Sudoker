package com.example.sudoker

import android.content.Context
import android.view.View
import android.widget.BaseAdapter
import android.view.ViewGroup
import com.example.sudoker.databinding.GridBlockBinding
import com.example.sudoker.databinding.GridItemBinding

class MatrixAdapter(var context: Context, var matrixList: List<Matrix>) : BaseAdapter() {
    override fun getCount(): Int {
        return matrixList.size
    }

    override fun getItem(i: Int): Any {
        return matrixList[i].data
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val v = View.inflate(context, R.layout.grid_item, null)
        val gridItemBinding = GridItemBinding.bind(v)
        gridItemBinding.cell.text = getItem(i).toString()
        return gridItemBinding.root
    }
}

class BlockAdapter(var context: Context, var blockList: List<Block>) : BaseAdapter() {
    override fun getCount(): Int {
        return blockList.size
    }

    override fun getItem(i: Int): List<Matrix> {
        return blockList[i].data
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val v = View.inflate(context, R.layout.grid_block, null)
        val gridBlockBinding= GridBlockBinding.bind(v)

//        val rows = 3
        val columns = 3
        gridBlockBinding.gridBlock.numColumns = columns

        val adapter = MatrixAdapter(context, getItem(i))
        gridBlockBinding.gridBlock.adapter = adapter
        return gridBlockBinding.root
    }
}
