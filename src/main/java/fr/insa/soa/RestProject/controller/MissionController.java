package fr.insa.soa.RestProject.controller;

import fr.insa.soa.RestProject.DataObjectAccess.MissionDAO;
import fr.insa.soa.RestProject.model.Mission;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/missions")
public class MissionController {
    // Logger for easier debugging
    private static final Logger logger = LoggerFactory.getLogger(MissionDAO.class);
    private MissionDAO missionDAO = new MissionDAO();

    // Ajouter une mission
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDemandOrProposition(Mission mission) {
        if (mission.getPersonInNeedId() <= 0 || mission.getName() == null || mission.getDescription() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Tous les champs nécessaires doivent être fournis : ID du demandeur, nom, description.")
                    .build();
        }

        try {
            Mission createdMission = missionDAO.createMission(mission);
            return Response.status(Response.Status.CREATED).entity(createdMission).build();
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la mission : {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de l'ajout de la mission : " + e.getMessage())
                    .build();
        }
    }

    // Récupérer toutes les missions
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mission> getAllMissions() {
        try {
            return missionDAO.getAllMissions();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des missions : {}", e.getMessage(), e);
            throw new WebApplicationException("Erreur lors de la récupération des missions.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Accepter ou refuser une mission
    @PUT
    @Path("/{missionId}/decision")
    public Response acceptOrRefuseMission(@PathParam("missionId") int missionId,
                                             @QueryParam("accepté") boolean accept,
                                             @QueryParam("motif") String motif) {
        try {
            int status = accept ? 2 : 3; // 2 : Acceptée, 3 : Refusée
            String description = accept ? null : motif;

            boolean updated = missionDAO.updateMissionStatus(missionId, status);
            if (updated) {
                return Response.ok(accept ? "Mission acceptée." : "Mission refusée. Motif : " + motif).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Mission introuvable.").build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la mission : {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la mise à jour de la mission : " + e.getMessage())
                    .build();
        }
    }

    // Supprimer une mission
    @DELETE
    @Path("/{missionId}")
    public Response supprimerMission(@PathParam("missionId") int missionId) {
        try {
            boolean deleted = missionDAO.deleteMission(missionId);
            if (deleted) {
                return Response.ok("Mission supprimée avec succès.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Mission introuvable.").build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la mission :  {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la suppression de la mission : " + e.getMessage())
                    .build();
        }
    }
}
