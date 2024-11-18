package fr.insa.soa;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="Missions")
public class MissionService {
    private List<Mission> missions = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>(); // Liste des avis
    private static int currentMissionId = 1;
    private static int currentReviewId = 1;

    @WebMethod
    public String postDemande(@WebParam(name = "userId") int userId,
                              @WebParam(name = "description") String description,
                              @WebParam(name = "name") String name) {
        Mission mission = new Mission();
        mission.setMissionId(currentMissionId++);
        mission.setPersonInNeedId(userId);
        mission.setDescription(description);
        mission.setName(name);
        mission.setStatut(0);
        missions.add(mission);
        return "Demande postée avec succès.";
    }
    
    @WebMethod
    public String postPropositionSpontanee(@WebParam(name = "volunteerId") int volunteerId,
                                           @WebParam(name = "description") String description,
                                           @WebParam(name = "name") String name) {
        Mission mission = new Mission();
        mission.setMissionId(currentMissionId++);
        mission.setHelperId(volunteerId);
        mission.setDescription(description);
        mission.setName(name);
        mission.setStatut(0); // 0 signifie "En attente d'approbation"
        missions.add(mission);
        return "Proposition spontanée postée avec succès.";
    }
    
    @WebMethod
    public List<Mission> afficherToutesLesMissions() {
        return new ArrayList<>(missions);
    }
    
    @WebMethod
    public String accepterOuRefuserMission(@WebParam(name = "missionId") int missionId,
                                           @WebParam(name = "accepte") boolean accepte,
                                           @WebParam(name = "motif") String motif) {
        for (Mission mission : missions) {
            if (mission.getMissionId() == missionId) {
                if (accepte) {
                    mission.setStatut(2); // 2 signifie "Acceptée"
                    return "Mission acceptée.";
                } else {
                    mission.setStatut(3); // 3 signifie "Refusée"
                    mission.setDescription(mission.getDescription() + " | Motif du refus : " + motif);
                    return "Mission refusée. Motif: " + motif;
                }
            }
        }
        return "Mission introuvable.";
    }
    
    @WebMethod
    public String supprimerMission(@WebParam(name = "missionId") int missionId) {
        boolean removed = missions.removeIf(mission -> mission.getMissionId() == missionId);
        if (removed) {
            return "Mission supprimée avec succès.";
        }
        return "Mission introuvable.";
    }
    

    @WebMethod
    public String laisserAvisSurMission(@WebParam(name = "missionId") int missionId,
                                        @WebParam(name = "userId") int userId,
                                        @WebParam(name = "rating") int rating,
                                        @WebParam(name = "comment") String comment) {
        // Vérification si la mission existe
        for (Mission mission : missions) {
            if (mission.getMissionId() == missionId) {
                // Création d'un nouvel avis
                Review review = new Review();
                review.setReviewId(currentReviewId++);
                review.setMissionId(missionId);
                review.setUserId(userId);
                review.setRating(rating);
                review.setComment(comment);

                // Ajout de l'avis à la liste
                reviews.add(review);
                return "Avis ajouté avec succès pour la mission ID : " + missionId;
            }
        }
        return "Mission introuvable pour laisser un avis.";
    }

    @WebMethod
    public List<Review> afficherAvisParMission(@WebParam(name = "missionId") int missionId) {
        // Filtrer les avis par ID de mission
        List<Review> missionReviews = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getMissionId() == missionId) {
                missionReviews.add(review);
            }
        }
        return missionReviews;
    }
    
    @WebMethod
    public String assignerVolontaire(@WebParam(name = "idVolunteer") int idVolunteer,
                                     @WebParam(name = "idMission") int idMission) {
        // Recherche de la mission dans la liste
        for (Mission mission : missions) {
            if (mission.getMissionId() == idMission) {
                // Vérification si la mission n'est pas déjà assignée
                if (mission.getHelperId() != 0) {
                    return "La mission est déjà assignée à un volontaire.";
                }
                
                // Assigner le volontaire à la mission
                mission.setHelperId(idVolunteer);
                mission.setStatut(1); // 1 pourrait signifier "En cours"
                return "Volontaire assigné avec succès à la mission.";
            }
        }
        return "Mission introuvable.";
    }
    
    @WebMethod
    public String assignerDemandeur(@WebParam(name = "idDemandeur") int idDemandeur,
                                    @WebParam(name = "idProposition") int idProposition) {
        // Recherche de la proposition dans la liste
        for (Mission mission : missions) {
            if (mission.getMissionId() == idProposition) {
                // Vérification si la proposition est déjà assignée à un demandeur
                if (mission.getPersonInNeedId() != 0) {
                    return "La proposition est déjà associée à un demandeur.";
                }
                
                // Assigner le demandeur à la proposition
                mission.setPersonInNeedId(idDemandeur);
                mission.setStatut(2); 
                return "Demandeur assigné avec succès à la proposition.";
            }
        }
        return "Proposition introuvable.";
    }



}
