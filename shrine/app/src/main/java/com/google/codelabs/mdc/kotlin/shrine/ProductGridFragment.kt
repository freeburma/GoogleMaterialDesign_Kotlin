package com.google.codelabs.mdc.kotlin.shrine

import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.codelabs.mdc.kotlin.shrine.network.ProductEntry
import com.google.codelabs.mdc.kotlin.shrine.staggeredgridlayout.StaggeredProductCardRecyclerViewAdapter
import kotlinx.android.synthetic.main.shr_product_grid_fragment.view.*

class ProductGridFragment : Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.shr_product_grid_fragment, container, false)

        //// Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            view.product_grid.background = context?.getDrawable(R.drawable.shr_product_grid_background_shape)
        }

        //// Set up the toolbar
        (activity as AppCompatActivity).setSupportActionBar(view.app_bar)

        //// Adding the motion which will drop and it will change the open/close icon automatically by "AccelerateInterpolator"
        view.app_bar.setNavigationOnClickListener(NavigationIconClickListener(
                                                                                activity!!,
                                                                                view.product_grid,
                                                                                AccelerateInterpolator(),
                                                                                ContextCompat.getDrawable(context!!, R.drawable.shr_branded_menu), // Menu Open Icon
                                                                                ContextCompat.getDrawable(context!!, R.drawable.shr_close_menu)    // Menu Close Icon
                                                                             )
                                                 )

        //// Set up the Recycler VIew
        view.recycler_view.setHasFixedSize(true)

        //// Adding the Staggered Layout - Horizontal Scrolling
        val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup()
        {
            override fun getSpanSize(position: Int): Int
            {
                return if (position % 3 == 2) 2 else 1
            }
        }

//        view.recycler_view.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false) // Vertical layout
        view.recycler_view.layoutManager = gridLayoutManager; // Staggered Layout

        //// Creating adapter
        val adapter = StaggeredProductCardRecyclerViewAdapter(ProductEntry.initProductEntryList(resources))

        view.recycler_view.adapter = adapter

        //// Creating Padding for cards
        val largePadding = resources.getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_large)
        val smallPadding = resources.getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_small)

        view.recycler_view.addItemDecoration(ProductGridItemDecoration(largePadding, smallPadding))


        return view;
    }// end onCreateView()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.shr_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }// end onCreateOptionsMenu()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}
