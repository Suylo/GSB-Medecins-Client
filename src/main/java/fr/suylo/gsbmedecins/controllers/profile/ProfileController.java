package fr.suylo.gsbmedecins.controllers.profile;

import fr.suylo.gsbmedecins.models.APIAccess;
import fr.suylo.gsbmedecins.models.Medecin;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {

    @FXML
    public Label doctorLastName, doctorName, doctorAddress, doctorPhone, doctorSpe, doctorID, doctorDepartment;

    private Integer id;

    public void loadData(Integer index) {
        this.setId(index);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void loadProfile() {
        Medecin unMedecin = APIAccess.getMedecinByID(getId());
        doctorID.setText("Profil N°" + unMedecin.getId().toString());
        doctorLastName.setText(unMedecin.getNom());
        doctorName.setText(unMedecin.getPrenom());
        doctorAddress.setText(unMedecin.getAdresse());
        doctorPhone.setText(unMedecin.getTel());
        doctorDepartment.setText(unMedecin.getDepartement().getNom() + " (" + unMedecin.getDepartement().getPays().getNom() + ")");
        if (unMedecin.getSpe() == null) {
            doctorSpe.setText("// Spécialité non renseignée.");
        } else {
            doctorSpe.setText(unMedecin.getSpe());
        }
    }
}
