package com.dicoding.pawscapstone.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dicoding.pawscapstone.database.Pet
import com.dicoding.pawscapstone.R

class PetAdapter(private val context: Context, private val petList: List<Pet>) :
    RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    // Listener untuk handle item click
    private var onItemClickListener: ((Pet) -> Unit)? = null

    // Setter untuk listener
    fun setOnItemClickListener(listener: (Pet) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]
        holder.petName.text = pet.name
        holder.bind(pet)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(pet)
        }
    }

    override fun getItemCount() = petList.size

    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petImage: ImageView = itemView.findViewById(R.id.imgPet)
        val petName: TextView = itemView.findViewById(R.id.tvPetName)

        fun bind(pet: Pet) {
            // Clear previous image (if any)
            petImage.setImageResource(0)
            petImage.setImageURI(null)

            // Load image based on whether it's a resource ID or a URI
            if (pet.getImageUri() != null) {
                Glide.with(context)
                    .load(Uri.parse(pet.getImageUri()))
                    .transform(CenterCrop(), RoundedCorners(50))
                    .into(petImage)
            } else {
                Glide.with(context)
                    .load(pet.getImageResource())
                    .transform(CenterCrop(), RoundedCorners(50))
                    .into(petImage)
            }
        }
    }
}