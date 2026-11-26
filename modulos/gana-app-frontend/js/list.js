"use strict";

const currencyFormatter = new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0
});

function escapeHtml(value) {
    const element = document.createElement("div");
    element.textContent = String(value ?? "");
    return element.innerHTML;
}

/** Construye una tarjeta accesible a partir de un registro del inventario. */
function createAnimalCard(animal) {
    return `
        <article class="animal-card">
            <div class="animal-card__header">
                <div>
                    <span class="animal-card__code">${escapeHtml(animal.code)}</span>
                    <h2>${escapeHtml(animal.name)}</h2>
                </div>
                <span class="status">${escapeHtml(animal.status)}</span>
            </div>
            <div class="animal-card__details">
                <div><small>Especie</small><strong>${escapeHtml(animal.species)}</strong></div>
                <div><small>Raza</small><strong>${escapeHtml(animal.breed)}</strong></div>
                <div><small>Peso</small><strong>${Number(animal.weight).toLocaleString("es-CO")} kg</strong></div>
                <div><small>Ubicación</small><strong>${escapeHtml(animal.location)}</strong></div>
            </div>
            <div class="animal-card__price">${currencyFormatter.format(animal.price)}</div>
            <div class="animal-card__actions">
                <a class="button button--secondary" href="form.html?id=${encodeURIComponent(animal.id)}">Editar</a>
                <button class="button button--danger" type="button" data-delete-id="${escapeHtml(animal.id)}">Eliminar</button>
            </div>
        </article>`;
}

function renderInventory() {
    const animals = GanaAnimalStorage.readAll();
    const list = document.getElementById("animalList");
    const emptyState = document.getElementById("emptyState");

    list.innerHTML = animals.map(createAnimalCard).join("");
    emptyState.classList.toggle("hidden", animals.length > 0);

    document.getElementById("totalAnimals").textContent = String(animals.length);
    document.getElementById("availableAnimals").textContent = String(
        animals.filter((animal) => animal.status === "Disponible").length
    );
    document.getElementById("inventoryValue").textContent = currencyFormatter.format(
        animals.reduce((total, animal) => total + Number(animal.price || 0), 0)
    );
}

function showOperationMessage() {
    const operation = sessionStorage.getItem("ganaApp.lastOperation");
    if (!operation) return;
    const message = document.getElementById("operationMessage");
    message.textContent = operation;
    message.classList.remove("hidden");
    sessionStorage.removeItem("ganaApp.lastOperation");
}

document.getElementById("animalList").addEventListener("click", (event) => {
    const deleteButton = event.target.closest("[data-delete-id]");
    if (!deleteButton) return;

    // La confirmación evita eliminaciones accidentales desde dispositivos táctiles.
    if (!window.confirm("¿Deseas eliminar este animal del inventario?")) return;
    const removed = GanaAnimalStorage.remove(deleteButton.dataset.deleteId);
    if (removed) {
        sessionStorage.setItem("ganaApp.lastOperation", "Animal eliminado correctamente.");
        renderInventory();
        showOperationMessage();
    }
});

document.addEventListener("DOMContentLoaded", () => {
    renderInventory();
    showOperationMessage();
});
