//========================================================//
//  1. Obtener los elemnetos del modal
//========================================================//

const fondoModal = document.getElementById("fondoModal");
const modalIcono = document.getElementById("modalIcono");
const modalTitulo = document.getElementById("modalTitulo");
const modalText = document.getElementById("modalText");
const modalAceptar = document.getElementById("modalAceptar");

//========================================================//
//  2. Función para mostrar mensaje
//========================================================//

function mostrarModal(tipo, titulo, texto){
    modalTitulo.textContent = titulo;
    modalText.textContent = texto;
    // Verifica si el mensaje es de error
    const esError = tipo === "error";
    // Si hay error, mostramos x
    // Si hay éxito, mostramos ✔
    modalIcono.textContent = esError ? "x" : "✔";
    // Aplicamos el estilo correspondiente
    modalIcono.className = "modal-icono " + (esError ? "error" : "exito");
    fondoModal.hidden = false;
    // Colocamos el cursosr en Aceptar
    modalAceptar.focus();
}

//========================================================//
//  2.a Cerrar el modal
//========================================================//
modalAceptar.addEventListener("click", function (){
    // Ocultamos el modal
    fondoModal.hidden = true;
});

//========================================================//
//  3. Leer los parametros de la URL
//========================================================//

// Obtenemos los parametros de la dirección web
const parametros = new URLSearchParams(window.location.search);
// Mensaje enviado despúes de la operación de éxito
const mensaje = parametros.get("mensaje");
// Mensaje enviado cuando ocurre un error
const error = parametros.get("error");

//========================================================//
//  5. Mostrar mensajes del CRUD
//========================================================//

// Resgitrar producto
if(mensaje === "guardado" || mensaje === "guardar"){
    mostrarModal("exito", "Producto registrado", "El producto fue registrado correctamente.");
}
// Altualizar producto
else if(mensaje === "actualizado" || mensaje === "actualido"){
    mostrarModal("exito", "Producto actualizado", "Los datos del producto fueron modificados.");
}
// Eliminar producto
else if(mensaje === "eliminado" || mensaje === "elimado"){
    mostrarModal("exito", "Producto eliminado", "El producto fue eliminado correctamente.");
}
// Error en los datos
else if(error === "error"){
    mostrarModal("error", "Datos incorrectos", "Debe verificar el nombre, precio y cantidad.");
}
// Producto no encontrado
else if(error === "noexiste"){
    mostrarModal("error", "Producto no encontrado", "El producto solicitado no existe.");
}

//========================================================//
//  6. Confirmar eliminación
//========================================================//
// Buscar los formularios de eliminación
const formulariosEliminar = document.querySelectorAll(".form-eliminar");

// Recorremos el formulario
formulariosEliminar.forEach(function (formulario){
    formulario.addEventListener("submit", function (event){
        // Solicitar confirmación
        const confirmar = confirm("¿Está seguro de eliminar este producto?");

        // Si preciona (Cancelar) detenemos el envío del formulario
        if(!confirmar){
            event.preventDefault();
        }

    });
});

//========================================================//
//  7. Limpiar los parametros de la URL
//========================================================//
// Evita que se repita el mensaje al actualizar
if(mensaje || error){
    window.history.replaceState({},document.title, window.location.pathname);
}


























