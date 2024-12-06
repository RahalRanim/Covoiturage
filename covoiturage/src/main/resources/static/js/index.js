// Ajout d'un gestionnaire d'événement pour la soumission du formulaire
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Empêcher l'envoi classique du formulaire

    // Récupérer les valeurs du formulaire
    const email = document.getElementById('exampleInputEmail1').value;
    const password = document.getElementById('exampleInputPassword1').value;

    // Envoyer une requête POST à l'API de connexion
    fetch('http://localhost:8082/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            mdp: password
        })
    })
        .then(response => response.json()) // Convertir la réponse en JSON
        .then(data => {
            // Cacher les messages d'erreur s'il y en a
            document.getElementById('error-message').style.display = 'none';

            if (data.message === "conducteur") {
                // Sauvegarder l'adresse e-mail dans localStorage
                localStorage.setItem('userEmail', email);

                // Redirection vers le tableau de bord du conducteur
                window.location.href = "/dashboardConducteur";
            } else if (data.message === "passager") {
                // Sauvegarder l'adresse e-mail dans localStorage
                localStorage.setItem('userEmail', email);

                // Redirection vers le tableau de bord du passager
                window.location.href = "/dashboardPassager";
            } else {
                // Afficher un message d'erreur si l'authentification a échoué
                document.getElementById('error-message').style.display = 'block';
                document.getElementById('error-message').textContent = "Utilisateur invalide !";
            }
        })
        .catch(error => {
            // Afficher un message d'erreur en cas de problème avec l'appel API
            document.getElementById('error-message').style.display = 'block';
            document.getElementById('error-message').textContent = "Une erreur est survenue, veuillez réessayer.";
        });
});