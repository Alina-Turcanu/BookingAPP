package booking.Services;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendBookingConfirmationAsync(String email, String hotelName, int roomNumber, LocalDate checkIn, LocalDate checkOut, double price) {
        String subject = "Confirmare rezervare cameră";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        String content = "<h2>Rezervare confirmată! 🏨</h2>" +
                "<p>Felicitări! Ai rezervat o cameră la <strong>" + hotelName + "</strong>.</p>" +
                "<p><strong>Număr cameră:</strong> " + roomNumber + "</p>" +
                "<p><strong>Check-in:</strong> " + checkIn.format(formatter) + "</p>" +
                "<p><strong>Check-out:</strong> " + checkOut.format(formatter) + "</p>" +
                "<p><strong>Preț total:</strong> " + price + " RON</p>" +
                "<p>Îți mulțumim și te așteptăm la hotel! 😊</p>";

        try {
            sendEmail(email, subject, content);
            System.out.println("E-mail de confirmare trimis către " + email);
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea e-mailului: " + e.getMessage());
        }
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("your-email@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
            System.out.println("E-mail trimis cu succes către " + to);
        } catch (MessagingException e) {
            System.err.println("Eroare la trimiterea email-ului: " + e.getMessage());
            e.printStackTrace();
        }
    }
}