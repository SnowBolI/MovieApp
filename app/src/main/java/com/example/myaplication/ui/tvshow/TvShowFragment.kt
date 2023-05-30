package com.example.myaplication.ui.tvshow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myaplication.data.client.ApiClient
import com.example.myaplication.data.model.ListResponse
import com.example.myaplication.data.model.TvShowResponse
import com.project.proyek_akhir_mobile_programming.databinding.FragmentTvShowBinding
import com.example.myaplication.ui.detail.DetailActivity
import com.example.myaplication.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowFragment : Fragment() {

    private var _binding: FragmentTvShowBinding? = null
    private lateinit var binding: FragmentTvShowBinding

    private lateinit var adapter: TvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (_binding == null){
            _binding = FragmentTvShowBinding.inflate(inflater, container, false)
            binding = _binding as FragmentTvShowBinding
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TvShowAdapter().apply {
            onClick {
                Intent(activity, DetailActivity::class.java).also { intent ->
                    intent.putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.data[1])
                    intent.putExtra(DetailActivity.EXTRA_DATA, it)
                    startActivity(intent)
                }
            }
        }

        getTvShow()
    }

    private fun getTvShow() {
        showLoading(true)

        ApiClient.instance.getTvShow()
            .enqueue(object : Callback<ListResponse<TvShowResponse>>{
                override fun onResponse(
                    call: Call<ListResponse<TvShowResponse>>,
                    response: Response<ListResponse<TvShowResponse>>
                ) {
                    if(response.isSuccessful){
                        binding.apply {
                            adapter.tvshow = response.body()?.results as MutableList<TvShowResponse>
                            rvTvshow.adapter = adapter
                            rvTvshow.setHasFixedSize(true)

                            showLoading(false)
                        }
                    }else{
                        activity?.showToast(response.message().toString())
                        showLoading(false)
                    }
                }

                override fun onFailure(call: Call<ListResponse<TvShowResponse>>, t: Throwable) {
                    activity?.showToast(t.message.toString())
                    showLoading(false)
                }

            })
    }

    private fun showLoading(state: Boolean){
        binding.apply {
            if (state){
                progressBar.visibility = View.VISIBLE
                rvTvshow.visibility = View.GONE
            }else{
                progressBar.visibility = View.GONE
                rvTvshow.visibility = View.VISIBLE
            }
        }
    }

}