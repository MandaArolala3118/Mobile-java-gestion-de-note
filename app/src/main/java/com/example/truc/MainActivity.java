package com.example.truc;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bean.Etudiant;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<Etudiant> adapter;
    private List<Etudiant> studentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Création de la liste d'étudiants
        studentList = new ArrayList<>();
        studentList.add(new Etudiant(1, "Alice", 5.0));
        studentList.add(new Etudiant(2, "Bob", 10));
        studentList.add(new Etudiant(3, "Mialy Nantenaina", 2.9));
        // Ajoutez d'autres étudiants ici

        // Création de l'adaptateur personnalisé
        adapter = new ArrayAdapter<Etudiant>(this, R.layout.list_item, studentList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Récupération de l'objet Student pour cette position
                Etudiant student = getItem(position);

                // Vérification si convertView est null
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                }

                // Remplissage des TextView avec les données de l'étudiant
                TextView obsTextView = convertView.findViewById(R.id.obsTextView);
                TextView numTextView = convertView.findViewById(R.id.numeroTextView);
                TextView nameTextView = convertView.findViewById(R.id.nameTextView);
                TextView averageTextView = convertView.findViewById(R.id.averageTextView);

                if (student != null) {
                    nameTextView.setText("Nom:         " + student.getName());
                    averageTextView.setText(String.valueOf("Moyenne : " + student.getAvarage()));
                    obsTextView.setText(String.valueOf(student.getObs()));
                    numTextView.setText(String.valueOf(student.getNumber()));
                }

                return convertView;
            }
        };

        // Configuration de l'adaptateur sur la ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        TextView textView = findViewById(R.id.moyenneClasse);
        textView.setText(this.avarageClasse());
        TextView textView1 = findViewById(R.id.maxMoyenne);
        textView1.setText(this.maxAverage());
        TextView textView2 = findViewById(R.id.minMoyenne);
        textView2.setText(this.minAverage());
        //Ouvrir le popup
        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStudentDialog();
            }
        });
        //hold listenner
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'étudiant sélectionné
                Etudiant student = studentList.get(position);

                // Afficher le popup pour la mise à jour
                showUpdatePopup(student);

                return true; // Indique que l'événement a été consommé
            }
        });



    }

    private void showAddStudentDialog() {
        // Créer une vue pour le formulaire d'ajout d'étudiant
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.add_student_dialog, null);

        // Créer le AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Ajouter un étudiant");

        // Définir les boutons du AlertDialog
        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Récupérer les valeurs entrées par l'utilisateur
                EditText numberEditText = dialogView.findViewById(R.id.numberEditText);
                EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
                EditText averageEditText = dialogView.findViewById(R.id.averageEditText);

                String number = numberEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String average = averageEditText.getText().toString();
                addStud(number, name, average);

                TextView textView = findViewById(R.id.moyenneClasse);
                textView.setText(avarageClasse());
                TextView textView1 = findViewById(R.id.maxMoyenne);
                textView1.setText(maxAverage());
                TextView textView2 = findViewById(R.id.minMoyenne);
                textView2.setText(minAverage());
            }
        });
        builder.setNegativeButton("Annuler", null);


        // Afficher le AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }
    private void addStud(String number, String name, String av){

        // Créer un nouvel objet Student avec les données entrées par l'utilisateur
        Etudiant newStudent = new Etudiant(Integer.parseInt(number), name, Double.parseDouble(av));

        // Ajouter l'étudiant à la liste
        studentList.add(newStudent);

        // Rafraîchir l'affichage de la ListView pour afficher le nouvel étudiant ajouté
        adapter.notifyDataSetChanged();
        System.out.println("Ajouter");
    }
    private void showUpdatePopup(final Etudiant student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mise à jour de l'étudiant");
        builder.setMessage("Voulez-vous procéder à une mise à jour avec l'étudiant " + student.getNumber() + "?");

        // Ajouter les boutons
        builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action pour modifier l'étudiant
                editStudent(student);

            }
        });

        builder.setNeutralButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action pour supprimer l'étudiant
                deleteStudent(student);
            }
        });

        builder.setNegativeButton("Annuler", null);

        // Afficher le AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void editStudent(Etudiant student) {
        // Créer une vue pour le formulaire d'ajout d'étudiant


        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.update_student_dialog, null);

        // Créer le AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Modifer un étudiant");

        // Définir les boutons du AlertDialog
        builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Récupérer les valeurs entrées par l'utilisateur
                EditText numberEditText = dialogView.findViewById(R.id.numberEditTextUpdate);
                EditText nameEditText = dialogView.findViewById(R.id.nameEditTextUpdate);
                EditText averageEditText = dialogView.findViewById(R.id.averageEditTextUpdate);

                String number = numberEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String average = averageEditText.getText().toString();
                updateStudent(Integer.parseInt(number), name, Double.parseDouble(average));
            }
        });
        builder.setNegativeButton("Annuler", null);


        // Afficher le AlertDialog
        // Afficher le AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Trouver les EditText dans la vue du dialogue de mise à jour
        EditText editTextnum = dialogView.findViewById(R.id.numberEditTextUpdate);
        EditText editTextname = dialogView.findViewById(R.id.nameEditTextUpdate);
        EditText editTextav = dialogView.findViewById(R.id.averageEditTextUpdate);

        // Définir le texte pour les EditText avec les valeurs de l'étudiant sélectionné
        editTextnum.setText(String.valueOf(student.getNumber()));
        editTextname.setText(student.getName());
        editTextav.setText(String.valueOf(student.getAvarage()));

    }
    private void updateStudent(int studentId, String newName, double newAverage) {
        // Parcourir la liste d'étudiants
        for (Etudiant student : studentList) {
            if (student.getNumber() == studentId) {
                // Mettre à jour les informations de l'étudiant
                student.setName(newName);
                student.setAvarage(newAverage);
                break; // Sortir de la boucle une fois que l'étudiant a été trouvé et mis à jour
            }
        }

        // Rafraîchir l'affichage de la ListView
        adapter.notifyDataSetChanged();

        TextView textView = findViewById(R.id.moyenneClasse);
        textView.setText(avarageClasse());
        TextView textView1 = findViewById(R.id.maxMoyenne);
        textView1.setText(this.maxAverage());
        TextView textView2 = findViewById(R.id.minMoyenne);
        textView2.setText(this.minAverage());
    }
    private void deleteStudent(Etudiant student) {
        // Supprimer l'étudiant de la liste
        studentList.remove(student);
        adapter.notifyDataSetChanged();

        TextView textView = findViewById(R.id.moyenneClasse);
        textView.setText(this.avarageClasse());
        TextView textView1 = findViewById(R.id.maxMoyenne);
        textView1.setText(this.maxAverage());
        TextView textView2 = findViewById(R.id.minMoyenne);
        textView2.setText(this.minAverage());
    }


    private String avarageClasse(){
        Double av = 0d;
        int i = 0;
        for (Etudiant student : studentList) {
            av += student.getAvarage();
            i++;
        }
        Double moy = av/i;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(moy);
    }
    private String minAverage(){
        Etudiant etudiantMin = Collections.min(studentList, (e1, e2) -> Double.compare(e1.getAvarage(), e2.getAvarage()));

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return decimalFormat.format(etudiantMin.getAvarage());
    }
    private String maxAverage(){

        Etudiant etudiantMax = Collections.max(studentList, (e1, e2) -> Double.compare(e1.getAvarage(), e2.getAvarage()));

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return decimalFormat.format(etudiantMax.getAvarage());
    }
}


