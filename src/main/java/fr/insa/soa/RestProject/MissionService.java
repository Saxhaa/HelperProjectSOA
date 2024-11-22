package fr.insa.soa.RestProject;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.ArrayList;
import java.util.List;

@Path("/missions")
//@Produces(MediaType.APPLICATION_JSON)//Static String
//@Consumes(MediaType.APPLICATION_JSON)
public class MissionService {
    private List<Mission> missions = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private static int currentMissionId = 1;
    private static int currentReviewId = 1;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDemandeOrProposition(Mission mission) {
        if (mission.getPersonInNeedId() <= 0 || mission.getName() == null || mission.getDescription() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Tous les champs nécessaires doivent être fournis : ID du demandeur, nom, description.")
                           .build();
        }

        mission.setMissionId(currentMissionId++);
        mission.setStatut(0); // 0 : en attente
        missions.add(mission);
        return Response.status(Response.Status.CREATED).entity(mission).build();
    }

    // Récupérer toutes les missions
    @GET
    public List<Mission> getAllMissions() {
        return missions;
    }

    // Accepter ou refuser une mission
    @PUT
    @Path("/{missionId}/decision")
    public Response accepterOuRefuserMission(@PathParam("missionId") int missionId, 
                                             @QueryParam("accepte") boolean accepte, 
                                             @QueryParam("motif") String motif) {
        for (Mission mission : missions) {
            if (mission.getMissionId() == missionId) {
                if (accepte) {
                    mission.setStatut(2); // Acceptée
                    return Response.ok("Mission acceptée.").build();
                } else {
                    mission.setStatut(3); // Refusée
                    mission.setDescription(mission.getDescription() + " | Motif du refus : " + motif);
                    return Response.ok("Mission refusée. Motif : " + motif).build();
                }
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Mission introuvable.").build();
    }

    // Supprimer une mission
    @DELETE
    @Path("/{missionId}")
    public Response supprimerMission(@PathParam("missionId") int missionId) {
        boolean removed = missions.removeIf(mission -> mission.getMissionId() == missionId);
        if (removed) {
            return Response.ok("Mission supprimée avec succès.").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Mission introuvable.").build();
    }

    // Ajouter un avis sur une mission
    @POST
    @Path("/{missionId}/review")
    public Response laisserAvisSurMission(@PathParam("missionId") int missionId, Review review) {
        for (Mission mission : missions) {
            if (mission.getMissionId() == missionId) {
                review.setReviewId(currentReviewId++);
                review.setMissionId(missionId);
                reviews.add(review);
                return Response.status(Response.Status.CREATED).entity(review).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Mission introuvable pour laisser un avis.").build();
    }

    // Récupérer les avis d'une mission
    @GET
    @Path("/{missionId}/reviews")
    public List<Review> getReviewsByMission(@PathParam("missionId") int missionId) {
        List<Review> missionReviews = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getMissionId() == missionId) {
                missionReviews.add(review);
            }
        }
        return missionReviews;
    }

    // Assigner un volontaire à une mission
    @PUT
    @Path("/{missionId}/assignVolunteer")
    public Response assignerVolontaire(@PathParam("missionId") int missionId, 
                                       @QueryParam("idVolunteer") int idVolunteer) {
        for (Mission mission : missions) {
            if (mission.getMissionId() == missionId) {
                if (mission.getHelperId() != 0) {
                    return Response.status(Response.Status.CONFLICT)
                                   .entity("La mission est déjà assignée à un volontaire.").build();
                }
                mission.setHelperId(idVolunteer);
                mission.setStatut(1); // En cours
                return Response.ok("Volontaire assigné avec succès.").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Mission introuvable.").build();
    }

    // Assigner un demandeur à une proposition
    @PUT
    @Path("/{missionId}/assignDemandeur")
    public Response assignerDemandeur(@PathParam("missionId") int missionId, 
                                      @QueryParam("idDemandeur") int idDemandeur) {
        for (Mission mission : missions) {
            if (mission.getMissionId() == missionId) {
                if (mission.getPersonInNeedId() != 0) {
                    return Response.status(Response.Status.CONFLICT)
                                   .entity("La proposition est déjà associée à un demandeur.").build();
                }
                mission.setPersonInNeedId(idDemandeur);
                mission.setStatut(2); // Acceptée
                return Response.ok("Demandeur assigné avec succès.").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Proposition introuvable.").build();
    }
}

