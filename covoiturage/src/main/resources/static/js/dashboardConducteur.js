// Récupérer l'email depuis le stockage local
const emailElement = document.getElementById('email');
const userEmail = localStorage.getItem('userEmail') || "email_non_disponible@example.com";
emailElement.textContent = userEmail;

window.onload = function() {
    recupererMoyenneNotes();
};

async function recupererMoyenneNotes() {
    const averageRatingElement = document.getElementById('average-rating');
    try {
        if (!userEmail) {
            throw new Error("Email utilisateur non défini");
        }

        const response = await fetch(`http://localhost:8082/api/moyenne-avis?email=${userEmail}`);
        console.log("Réponse de l'API reçue:", response);

        if (!response.ok) {
            throw new Error("Erreur dans la réponse de l'API");
        }

        const moyenneAvis = await response.json();

        if (moyenneAvis != null) {
            averageRatingElement.textContent = moyenneAvis.toFixed(2);
        } else {
            throw new Error("Moyenne des avis invalide");
        }

    } catch (error) {
        console.error("Erreur lors de la récupération de la moyenne des notes:", error);
        averageRatingElement.textContent = "Erreur";
    }
}







// Fonction pour afficher les trajets du conducteur
async function afficherTrajets() {
    try {
        const response = await fetch(`http://localhost:8082/api/mes-trajets?email=${userEmail}`);
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération des trajets");
        }

        const trajets = await response.json();
        const trajetsContainer = document.getElementById('trajets-container');

        if (trajets.length === 0) {
            trajetsContainer.innerHTML = "<p>Aucun trajet trouvé pour ce conducteur.</p>";
            return;
        }

        for (let trajet of trajets) {
            const isTrajetValid = await trajetIsValid(trajet.idT); // Attendre la validation du trajet

            const card = document.createElement('div');
            card.classList.add('card');

            card.innerHTML = `
                <img src="https://img.freepik.com/vecteurs-libre/illustration-concept-covoiturage_114360-9238.jpg" class="card-img-top" alt="Un trajet">
                <div class="card-body">
                    <h5 class="card-title">De: <font color="#48a347"><i> ${trajet.depart} </i></font></h5>
                    <h5 class="card-title">À: <font color="#48a347"><i>${trajet.destination} </i></font></h5>
                    <p class="card-text">
                       <b>Date:</b>  ${trajet.date} <br>
                        <b>Heure:</b> ${trajet.time} <br>
                        <b>Places disponibles:</b> ${trajet.placeDispo} <br>
                        <b>Prix par place:</b> ${trajet.prixPlace} <i>Dt</i> <br>
                        ${trajet.com && trajet.com.trim() !== "" ? `<b>Commentaire:</b> ${trajet.com} ` : ""}

                    </p>
                    ${isTrajetValid ? `<a href="#" class="btn btn-outline-primary" onclick="ouvrirModal(${trajet.idT}, '${userEmail}')">Évaluer ⭐</a>` : ''}
                </div>
            `;
            trajetsContainer.appendChild(card);
        }
    } catch (error) {
        console.error("Erreur:", error);
        document.getElementById('trajets-container').innerHTML = "<p>Vous n'avez pas encore de trajets.</p>";
    }
}


afficherTrajets();

// Fonction de déconnexion
function deconnecter() {
    localStorage.removeItem('userEmail');
    window.location.href = '/';
}

// l'affichage du model
function ouvrirModal(idTrajet,userEmail) {
    const modal = document.getElementById('evalmodel');
    modal.style.display = 'block';

    $('#evalmodel').modal('show');

    const confirmerEvaluationBtn = document.getElementById('confirmerEvaluation');
    confirmerEvaluationBtn.onclick = function() {
        EvaleurTrajet(idTrajet, userEmail);
    };

    // Fermeture du modal lorsque le bouton "Close" (icône ×) est cliqué
    const closeBtn = document.querySelector('.close');
    closeBtn.onclick = function() {
        modal.style.display = 'none';
    };

    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    };
}

//Evaluer
function EvaleurTrajet(idTrajet,userEmail) {
    const note = document.getElementById('note').value;
    const descp = document.getElementById('descp').value;

    const avisRequest = {
        note: note,
        descp: descp
    };


    fetch(`http://localhost:8082/api/evaluer?emailUtilisateur=${userEmail}&idTrajet=${idTrajet}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(avisRequest)
    })
        .then(async response => {
            const result = await response.text();

            if (!response.ok) {
                throw new Error(result);
            }

            return result;
        })
        .then(message => {
            alert(message);
            $('#evalmodel').modal('hide');
        })
        .catch(error => {
            console.error('Erreur lors de l\'évaluation :', error.message);
            alert("Erreur : " + error.message);
        });
}

async function trajetIsValid(idTrajet) {
    try {
        const response = await fetch(`http://localhost:8082/api/trajet-is-valid?idT=${idTrajet}`);

        if (!response.ok) {
            throw new Error("Erreur lors de la vérification de la validité du trajet");
        }

        // Récupérer et retourner la réponse JSON (vrai ou faux)
        const isValid = await response.json();
        return isValid;  // Retourne directement la valeur
    } catch (error) {
        console.error("Erreur dans la fonction trajetIsValid:", error);
        return false;  // Si une erreur se produit, on retourne false
    }
}