package fr.insa.soa;

public class Mission {
	private int missionId;
    private int personInNeedId;
    private int helperId;
    private String description;
    public int getMissionId() {
		return missionId;
	}
	public void setMissionId(int missionId) {
		this.missionId = missionId;
	}
	public int getPersonInNeedId() {
		return personInNeedId;
	}
	public void setPersonInNeedId(int personInNeedId) {
		this.personInNeedId = personInNeedId;
	}
	public int getHelperId() {
		return helperId;
	}
	public void setHelperId(int helperId) {
		this.helperId = helperId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatut() {
		return statut;
	}
	public void setStatut(int statut) {
		this.statut = statut;
	}
	private String name;
    private int statut;

}
