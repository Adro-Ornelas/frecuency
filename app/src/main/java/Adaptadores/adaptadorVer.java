package Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frecuency.CardArtista;
import com.example.frecuency.R;

import Global.Listas;

public class adaptadorVer extends RecyclerView.Adapter<adaptadorVer.MiniActivity> {
    public Context context;
    @NonNull
    @Override
    public adaptadorVer.MiniActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myVer = View.inflate(context, R.layout.view_holder_ver, null);
        adaptadorVer.MiniActivity miniMini = new adaptadorVer.MiniActivity(myVer);
        return miniMini;
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorVer.MiniActivity holder, int position) {
        final int pos = position;
        // Carga nombre artístico en TextView:
        holder.txt_artista.setText(Listas.listaArtistas.get(position).getNombreArt());
        // ClickListener en Layout para mostrar más información:
        holder.linearLayout_artista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navega a activity CardArtista
                Intent aCardA = new Intent(context, CardArtista.class);
                // Envía index del arrayList
                aCardA.putExtra("pos", pos);
                context.startActivity(aCardA);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Listas.listaArtistas.size();
    }

    public class MiniActivity extends RecyclerView.ViewHolder {
        TextView txt_artista;
        LinearLayout linearLayout_artista;
        public MiniActivity(@NonNull View itemView) {
            super(itemView);
            txt_artista = itemView.findViewById(R.id.txtv_artista);
            linearLayout_artista = itemView.findViewById(R.id.linearLayout_artista);
        }
    }
}
