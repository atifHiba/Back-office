package com.example.backoffice.controller;

import com.example.backoffice.dao.*;
import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.entity.City;
import com.example.backoffice.entity.Role;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.backoffice.entity.Role.CENTER_MANAGER;
import static com.example.backoffice.entity.Role.USER;

@Controller
public class ReportController {

    @Autowired
    private DonationCenterRepository centerRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;
    @GetMapping("/reports/download")
    public ResponseEntity<ByteArrayResource> generatePdf() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 70, 50); // marge un peu différente pour header
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // Couleurs personnalisées (rouge sang + bleu ciel)
        Color bloodRed = new Color(200, 23, 26);
        Color noskyBlue = new Color(253, 248, 248);
        Color skyBlue = new Color(51, 185, 223);
        Color blue = new Color(0, 51, 102);

        // Polices (taille petite et claire)
        Font logoFont = new Font(Font.HELVETICA, 18, Font.BOLD, bloodRed);
        Font appTitleFont = new Font(Font.HELVETICA, 19, Font.BOLD, bloodRed);
        Font sectionTitleFont1 = new Font(Font.HELVETICA, 22, Font.BOLD, blue);
        Font sectionTitleFont = new Font(Font.HELVETICA, 18, Font.BOLD, skyBlue);

        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.DARK_GRAY);
        Font valueFont = new Font(Font.HELVETICA, 10, Font.NORMAL, bloodRed);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);

        // --- HEADER PERSONNALISE ---
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1f, 9f});
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        headerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        // Logo goutte de sang (simple "droplet" unicode, car pas possible d'intégrer du HTML ici)
        Phrase logoPhrase = new Phrase("\uD83D\uDCA7", logoFont); // goutte d'eau rouge (💧) unicode alternative
        PdfPCell logoCell = new PdfPCell(logoPhrase);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPaddingLeft(5);
        headerTable.addCell(logoCell);

        // Titre de l'application
        PdfPCell titleCell = new PdfPCell(new Phrase("SangConnect", appTitleFont));
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setPaddingLeft(5);
        headerTable.addCell(titleCell);

        document.add(headerTable);

        // Ligne séparatrice horizontale fine
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(bloodRed);
        separator.setLineWidth(1f);
        separator.setPercentage(100f);
        separator.setOffset(-5);
        document.add(new Chunk(separator));

        // Espacement sous header
        document.add(new Paragraph(" "));

        // Titre principal du rapport centré, plus petit que avant (pour garder légèreté)
        Paragraph reportTitle = new Paragraph("Rapport Global des Centres de Don de Sang", sectionTitleFont1);
        reportTitle.setAlignment(Element.ALIGN_CENTER);
        reportTitle.setSpacingAfter(10);
        document.add(reportTitle);

        Paragraph date = new Paragraph("Date de génération : " + LocalDate.now(), normalFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(20);
        document.add(date);

        // --- Statistiques globales ---
        Paragraph globalTitle = new Paragraph("Statistiques Globales", sectionTitleFont);
        globalTitle.setSpacingAfter(12);
        globalTitle.setIndentationLeft(10);
        document.add(globalTitle);

        // Récupération des données (ton code ici)
        long totalUsers = userRepository.count();
        long normalUsers = userRepository.countByRole(USER);
        long centerManagers = userRepository.countByRole(CENTER_MANAGER);
        long totalRequests = requestRepository.count();
        long saturatedRequests = requestRepository.countByRequiredBloodUnits(0);
        long nonSaturatedRequests = requestRepository.countByRequiredBloodUnitsGreaterThan(0);
        long totalDonations = donationRepository.count();

        // Tableau de stats globales, largeur 65%, sans bordure, avec alternance couleurs claires
        PdfPTable globalStatsTable = new PdfPTable(2);
        globalStatsTable.setWidthPercentage(65);
        globalStatsTable.setSpacingAfter(25);
        globalStatsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        globalStatsTable.setWidths(new float[]{3, 1});

        addColoredCell(globalStatsTable, "Nombre total d'utilisateurs :", labelFont, Color.WHITE);
        addColoredCell(globalStatsTable, String.valueOf(totalUsers), valueFont, noskyBlue);

        addColoredCell(globalStatsTable, "Nombre d'utilisateurs normaux :", labelFont, Color.WHITE);
        addColoredCell(globalStatsTable, String.valueOf(normalUsers), valueFont, noskyBlue);

        addColoredCell(globalStatsTable, "Nombre de gestionnaires de centre :", labelFont, Color.WHITE);
        addColoredCell(globalStatsTable, String.valueOf(centerManagers), valueFont, noskyBlue);

        addColoredCell(globalStatsTable, "Nombre total de demandes :", labelFont, Color.WHITE);
        addColoredCell(globalStatsTable, String.valueOf(totalRequests), valueFont, noskyBlue);

        addColoredCell(globalStatsTable, "Demandes saturées :", labelFont, Color.WHITE);
        addColoredCell(globalStatsTable, String.valueOf(saturatedRequests), valueFont, noskyBlue);

        addColoredCell(globalStatsTable, "Demandes non saturées :", labelFont, Color.WHITE);
        addColoredCell(globalStatsTable, String.valueOf(nonSaturatedRequests), valueFont, noskyBlue);

        addColoredCell(globalStatsTable, "Nombre total de dons :", labelFont, Color.WHITE);
        addColoredCell(globalStatsTable, String.valueOf(totalDonations), valueFont, noskyBlue);

        document.add(globalStatsTable);

        // --- Détails par ville ---
        Paragraph citySectionTitle = new Paragraph("Détails par Ville et Centres", sectionTitleFont);
        citySectionTitle.setSpacingBefore(10);
        citySectionTitle.setSpacingAfter(12);
        citySectionTitle.setIndentationLeft(10);
        document.add(citySectionTitle);

        List<City> cities = cityRepository.findAll();
        int cityCounter = 1;

        for (City city : cities) {
            Paragraph cityTitle = new Paragraph(cityCounter + ". " + city.getName(),
                    new Font(Font.HELVETICA, 14, Font.BOLD, new Color(0, 51, 102))); // bleu foncé
            cityTitle.setSpacingBefore(8);
            cityTitle.setSpacingAfter(6);
            cityTitle.setIndentationLeft(15);
            document.add(cityTitle);

            List<DonationCenter> centers = centerRepository.findByCity(city);
            if (centers.isEmpty()) {
                Paragraph noCenter = new Paragraph("Aucun centre disponible dans cette ville.", normalFont);
                noCenter.setIndentationLeft(25);
                document.add(noCenter);
                cityCounter++;
                continue;
            }

            for (DonationCenter center : centers) {
                Paragraph centerTitle = new Paragraph("• Centre : " + center.getName(),
                        new Font(Font.HELVETICA, 12, Font.BOLD, bloodRed));
                centerTitle.setIndentationLeft(25);
                centerTitle.setSpacingBefore(4);
                centerTitle.setSpacingAfter(6);
                document.add(centerTitle);

                long centerRequests = requestRepository.countByDonationCenter(center);
                long centerSaturatedRequests = requestRepository.countByDonationCenterAndRequiredBloodUnits(center, 0);
                long centerNonSaturatedRequests = requestRepository.countByDonationCenterAndRequiredBloodUnitsGreaterThan(center, 0);
                long centerDonations = donationRepository.countByDonationCenter(center);

                PdfPTable centerStatsTable = new PdfPTable(2);
                centerStatsTable.setWidthPercentage(55);
                centerStatsTable.setWidths(new float[]{3, 1});
                centerStatsTable.setSpacingAfter(12);
                centerStatsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                centerStatsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                addColoredCell(centerStatsTable, "Nombre de demandes :", labelFont, Color.WHITE);
                addColoredCell(centerStatsTable, String.valueOf(centerRequests), valueFont, noskyBlue);

                addColoredCell(centerStatsTable, "Demandes saturées :", labelFont, Color.WHITE);
                addColoredCell(centerStatsTable, String.valueOf(centerSaturatedRequests), valueFont, noskyBlue);

                addColoredCell(centerStatsTable, "Demandes non saturées :", labelFont, Color.WHITE);
                addColoredCell(centerStatsTable, String.valueOf(centerNonSaturatedRequests), valueFont, noskyBlue);

                addColoredCell(centerStatsTable, "Nombre de dons :", labelFont, Color.WHITE);
                addColoredCell(centerStatsTable, String.valueOf(centerDonations), valueFont, noskyBlue);

                document.add(centerStatsTable);
            }
            cityCounter++;
        }

        document.close();

        byte[] pdfData = baos.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapport_global_centres.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfData.length)
                .body(new ByteArrayResource(pdfData));
    }

    // Méthode utilitaire ajoutant cellule avec couleur de fond
    private void addColoredCell(PdfPTable table, String text, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(6);
        table.addCell(cell);
    }}
