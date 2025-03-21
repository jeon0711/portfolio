    import { httpRequest } from "./httpRequest.js";
    document.getElementById("email-form").addEventListener("submit", function(event) {
        event.preventDefault();
        const email = document.getElementById("email").value;
       try {
           const encodedEmail = encodeURIComponent(email);
           location.replace(`/synthesis/${encodedEmail}`);
       } catch (e) {
           console.error("Redirect error:", e);
           alert("Redirect error: " + e.message);
       }

    });