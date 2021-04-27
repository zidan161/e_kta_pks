package com.example.myapplication

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(), ProfilePresenter.ProfileView {

    private var binding: FragmentProfileBinding? = null
    private val bind get() = binding!!
    private var id: String? = null
    private lateinit var presenter: ProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        id = activity?.intent?.getStringExtra(MainActivity.REQUEST_ID)!!

        presenter = ProfilePresenter(this)
        presenter.getAnggota(id)

        bind.slMain.setOnRefreshListener {
            presenter.getAnggota(id)
        }

        Picasso.get().load("https://www.mantruckandbus.com/fileadmin/media/bilder/02_19/219_05_busbusiness_interviewHeader_1485x1254.jpg").into(bind.profileImage)

        bind.btnToCard.setOnClickListener {
            val intent = Intent(context, CardActivity::class.java)
            intent.putExtra(MainActivity.REQUEST_ID, id)
            startActivity(intent)
        }

        bind.btnLogout.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Apakah anda yakin ingin keluar?")
                .setPositiveButton("Yes") { _, _ ->
                    val pref = requireContext().getSharedPreferences(AuthActivity.PREFS_NAME, Context.MODE_PRIVATE)
                    pref.edit().putString(AuthActivity.ID, null).apply()
                    val intent = Intent(context, AuthActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        bind.fbEdit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(EditActivity.ID, id)
            startActivity(intent)
        }

        bind.profileImage.setOnClickListener {
            val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intentGallery, 100)
        }
        return view
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null){
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data.data)
            requireContext().getSharedPreferences(AuthActivity.PREFS_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString("image", bitmap.toString())
                    .apply()
            Picasso.get().load(data.data).into(bind.profileImage)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setProfile(profile: Profile?) {
        if (profile != null) {
            val village = if (profile.village.toString() == "[]") {
                null
            } else {
                profile.village[0].name
            }

            bind.tvKta.text = "No. Kta: $id"
            bind.tvName.text = profile.name
            bind.tvTelpVal.text = profile.mobile
            bind.tvEmailVal.text = profile.email
            bind.tvAddress2.text = "${profile.street}, $village, ${profile.district[0].name}, ${profile.city[0].name}, ${profile.province[0].name}"

            bind.slMain.isRefreshing = false
        }
    }
}