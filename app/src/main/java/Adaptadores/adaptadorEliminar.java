package Adaptadores;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.frecuency.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import Global.Listas;
import POJO.Artista;

public class adaptadorEliminar extends RecyclerView.Adapter<adaptadorEliminar.MiniActivity> {
    public Context context;
    @NonNull
    @Override
    public adaptadorEliminar.MiniActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myVer = View.inflate(context, R.layout.view_holder_eliminar, null);
        adaptadorEliminar.MiniActivity miniMini = new adaptadorEliminar.MiniActivity(myVer);
        return miniMini;
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorEliminar.MiniActivity holder, int position) {

        final int pos = position;
        Artista artista = Listas.listaArtistas.get(pos);
        holder.txtv_dartista.setText(artista.getNombreArt()
        + " - " + artista.getCiudadShow());
        holder.checkDelete.setChecked(false);
        holder.checkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si se selecciona incluir a lista de baja
                if(holder.checkDelete.isChecked())
                    Listas.listaBajasArtistas.add(Listas.listaArtistas.get(pos));
                else
                    Listas.listaBajasArtistas.remove(Listas.listaArtistas.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Listas.listaArtistas.size();
    }

    public class MiniActivity extends RecyclerView.ViewHolder {
        TextView txtv_dartista;
        CheckBox checkDelete;
        public MiniActivity(@NonNull View itemView) {
            super(itemView);
            txtv_dartista = itemView.findViewById(R.id.txtv_dartista);
            checkDelete = itemView.findViewById(R.id.checkDelete1);
        }
    }
}
