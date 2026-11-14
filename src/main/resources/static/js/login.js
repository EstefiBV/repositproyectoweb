
document.addEventListener("DOMContentLoaded", function () {

    const formulario = document.getElementById("formLogin");
    const fondoModal = document.getElementById("fondoModal");
    const modalIcono = document.getElementById("modalIcono");
    const modalTitulo = document.getElementById("modalTitulo");
    const modalText = document.getElementById("modalText");
    const modalAceptar = document.getElementById("modalAceptar");

    function mostrarModal(tipo, titulo, mensaje) {

        const esError = tipo === "error";

        modalTitulo.textContent = titulo;
        modalText.textContent = mensaje;

        modalIcono.textContent = esError ? "✖" : "✔";

        modalIcono.className =
            "modal-icono " + (esError ? "error" : "exito");

        fondoModal.hidden = false;
        modalAceptar.focus();
    }

    modalAceptar.addEventListener("click", function () {
        fondoModal.hidden = true;
        document.getElementById("usuario").focus();
    });

    formulario.addEventListener("submit", function (event) {

        const usuario = document
            .getElementById("usuario").value.trim();

        const contrasena = document
            .getElementById("contrasena").value;

        if (!usuario || !contrasena) {

            event.preventDefault();

            mostrarModal(
                "error",
                "Campos incompletos",
                "Debe ingresar usuario y contraseña."
            );

            return;
        }

        console.log("Enviando formulario a Spring Security");
    });

    const parametros =
        new URLSearchParams(window.location.search);

    if (parametros.has("error")) {

        mostrarModal(
            "error",
            "Error de autenticación",
            "Spring Security rechazó el inicio de sesión."
        );

    } else if (parametros.has("logout")) {

        mostrarModal(
            "exito",
            "Sesión finalizada",
            "Ha cerrado sesión correctamente."
        );
    }

    if (parametros.has("error") || parametros.has("logout")) {
        window.history.replaceState(
            {},
            "",
            window.location.pathname
        );
    }

});

