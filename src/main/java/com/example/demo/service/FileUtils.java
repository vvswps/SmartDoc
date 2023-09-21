package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(FileUtils.class);

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
                System.out.println(fileType);
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
                        System.out.println("\n\n\nAdding CWS\n\n\n");
                        conferenceWorkshopSeminarFiles.add(file);
                        break;
                    case INDUSTRIALVISIT:
                        industrialVisitsFiles.add(file);
                        break;
                    case GUESTLECTURE:
                        guestLectureFiles.add(file);
                        break;
                    default:
                        logger.warn("Unknown file type");
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
            model.addAttribute("conferenceWorkshopSeminarFiles", conferenceWorkshopSeminarFiles);
        }
        if (!industrialVisitsFiles.isEmpty()) {
            model.addAttribute("industrialVisitsFiles", industrialVisitsFiles);
        }
        if (!guestLectureFiles.isEmpty()) {
            model.addAttribute("guestLectureFiles", guestLectureFiles);
        }
    }

}
