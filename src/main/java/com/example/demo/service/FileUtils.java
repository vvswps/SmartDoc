package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.model.DatabaseFile;
import com.example.demo.model.DatabaseFile.FileType;
import com.example.demo.repository.DatabaseFileRepository;
import com.example.demo.repository.personalRepository;
import com.example.demo.model.PersonalDtls;
import com.example.demo.model.UserDtls;

@Service
public class FileUtils {

    @Autowired
    private DatabaseFileRepository fileRepo;

    @Autowired
    private personalRepository personalRepository;

    public void populateFileListsAndAddToModel(UserDtls user, Model model) {
        List<DatabaseFile> awardsFiles = new ArrayList<>();
        List<DatabaseFile> patentFiles = new ArrayList<>();
        List<DatabaseFile> researchFiles = new ArrayList<>();
        List<DatabaseFile> bookFiles = new ArrayList<>();
        List<DatabaseFile> fdpFiles = new ArrayList<>();
        List<DatabaseFile> sttpFiles = new ArrayList<>();
        List<DatabaseFile> qipFiles = new ArrayList<>();
        List<DatabaseFile> conferenceWorkshopSeminarFiles = new ArrayList<>();
        List<DatabaseFile> industrialVisitsFiles = new ArrayList<>();
        List<DatabaseFile> guestLectureFiles = new ArrayList<>();
        PersonalDtls puser = personalRepository.findById(user.getId());
        model.addAttribute("puser", puser);

        List<DatabaseFile> files = fileRepo.findByUser(user);

        try {
            for (DatabaseFile file : files) {
                FileType fileType = file.getType();
                switch (fileType) {
                    case AWARD:
                        awardsFiles.add(file);
                        break;
                    case PATENT:
                        patentFiles.add(file);
                        break;
                    case RESEARCH_PAPER:
                        researchFiles.add(file);
                        break;
                    case BOOK_OR_CHAPTER:
                        bookFiles.add(file);
                        break;
                    case FDP:
                        fdpFiles.add(file);
                        break;
                    case STTP:
                        sttpFiles.add(file);
                        break;
                    case QIP:
                        qipFiles.add(file);
                        break;
                    case CONFERENCE_WORKSHOP_SEMINAR:
                        conferenceWorkshopSeminarFiles.add(file);
                        break;
                    case INDUSTRIALVISIT:
                        industrialVisitsFiles.add(file);
                        break;
                    case GUESTLECTURE:
                        guestLectureFiles.add(file);
                        break;
                    default:
                        System.out.println("Unknown file type");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!awardsFiles.isEmpty()) {
            model.addAttribute("awardsFiles", awardsFiles);
        }
        if (!patentFiles.isEmpty()) {
            model.addAttribute("patentFiles", patentFiles);
        }
        if (!researchFiles.isEmpty()) {
            model.addAttribute("researchFiles", researchFiles);
        }
        if (!bookFiles.isEmpty()) {
            model.addAttribute("bookFiles", bookFiles);
        }
        if (!fdpFiles.isEmpty()) {
            model.addAttribute("fdpFiles", fdpFiles);
        }
        if (!sttpFiles.isEmpty()) {
            model.addAttribute("sttpFiles", sttpFiles);
        }
        if (!qipFiles.isEmpty()) {
            model.addAttribute("qipFiles", qipFiles);
        }
        if (!conferenceWorkshopSeminarFiles.isEmpty()) {
            System.out.println("\n\n\nNo CWS\n\n\n");

            model.addAttribute("conferenceWorkshopSeminarFiles", conferenceWorkshopSeminarFiles);
        }
        if (!industrialVisitsFiles.isEmpty()) {
            System.out.println("\n\n\nNo Industry\n\n\n");
            model.addAttribute("industrialVisitsFiles", industrialVisitsFiles);
        }
        if (!guestLectureFiles.isEmpty()) {
            System.out.println("\n\n\nNo Lecture\n\n\n");

            model.addAttribute("guestLectureFiles", guestLectureFiles);
        }
    }

}
