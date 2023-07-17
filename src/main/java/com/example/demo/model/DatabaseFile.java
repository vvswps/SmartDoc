package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
public class DatabaseFile {

	public enum FileType {
		RESEARCH_PAPER,
		BOOK_OR_CHAPTER,
		AWARD,
		PATENT,
		FDP,
		STTP,
		QIP,
		CONFERENCE_WORKSHOP_SEMINAR,
		GUESTLECTURE,
		INDUSTRIALVISIT,
		PROFILE_PICTURE
	}

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2") // this is for generating random uuid
	private String id;

	// why use uuid2 instead of uuid?
	// The uuid2 strategy also generates a UUID, but it uses a different
	// representation. Instead of the standard string representation, it uses a
	// shorter 36-character sequence consisting of alphanumeric characters without
	// hyphens (e.g., 550e8400e29b41d4a716446655440000). Hibernate generates this
	// type of UUID without relying on the database's built-in UUID support.

	private String title;
	private String date;
	// this is the enum type i.e file category like research paper, book or chapter
	private FileType type;
	private String publicationName;
	private String awardingInstitution;
	private String publicationType;

	private String ISSN;
	private String DOI;
	private String Volume;
	private String ISBN;
	private String nature; // online / offline
	private String eventRole;
	private String eventType;
	private String durationFrom;
	private String durationTo;
	private int noOfDays;
	private String organizedBy;
	private String patentStatus;
	private String patentNumber;
	private String lectureTopic;
	private String placeOrEvent;
	private String noOfStudentsVisited;

	private String fileName;

	// this is the file type i.e pdf, docx, jpg etc
	private String fileType;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] data;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePicture;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserDtls user;

	public String getPatentStatus() {
		return patentStatus;
	}

	public void setPatentStatus(String patentStatus) {
		this.patentStatus = patentStatus;
	}

	public String getPatentNumber() {
		return patentNumber;
	}

	public void setPatentNumber(String patentNumber) {
		this.patentNumber = patentNumber;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public String getPublicationName() {
		return publicationName;
	}

	public void setPublicationName(String publicationName) {
		this.publicationName = publicationName;
	}

	public String getAwardingInstitution() {
		return awardingInstitution;
	}

	public void setAwardingInstitution(String awardingInstitution) {
		this.awardingInstitution = awardingInstitution;
	}

	public String getPublicationType() {
		return publicationType;
	}

	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}

	public String getISSN() {
		return ISSN;
	}

	public void setISSN(String iSSN) {
		ISSN = iSSN;
	}

	public String getDOI() {
		return DOI;
	}

	public void setDOI(String dOI) {
		DOI = dOI;
	}

	public String getVolume() {
		return Volume;
	}

	public void setVolume(String volume) {
		Volume = volume;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getDurationFrom() {
		return durationFrom;
	}

	public void setDurationFrom(String durationFrom) {
		this.durationFrom = durationFrom;
	}

	public String getDurationTo() {
		return durationTo;
	}

	public void setDurationTo(String durationTo) {
		this.durationTo = durationTo;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getOrganizedBy() {
		return organizedBy;
	}

	public void setOrganizedBy(String organizedBy) {
		this.organizedBy = organizedBy;
	}

	public UserDtls getUserId() {
		return user;
	}

	public void setUserId(UserDtls user) {
		this.user = user;
	}

	public DatabaseFile() {

	}

	public DatabaseFile(String fileName, String fileType, byte[] data) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getEventRole() {
		return eventRole;
	}

	public void setEventRole(String eventRole) {
		this.eventRole = eventRole;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getLectureTopic() {
		return lectureTopic;
	}

	public void setLectureTopic(String lectureTopic) {
		this.lectureTopic = lectureTopic;
	}

	public String getPlaceOrEvent() {
		return placeOrEvent;
	}

	public void setPlaceOrEvent(String placeOrEvent) {
		this.placeOrEvent = placeOrEvent;
	}

	public String getNoOfStudentsVisited() {
		return noOfStudentsVisited;
	}

	public void setNoOfStudentsVisited(String noOfStudentsVisited) {
		this.noOfStudentsVisited = noOfStudentsVisited;
	}

	public UserDtls getUser() {
		return user;
	}

	public void setUser(UserDtls user) {
		this.user = user;
	}

}
