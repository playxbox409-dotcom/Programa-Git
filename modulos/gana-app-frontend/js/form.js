"use strict";

const form = document.getElementById("animalForm");
const formMessage = document.getElementById("formMessage");
const priceInput = document.getElementById("price");
const formattedPrice = document.getElementById("formattedPrice");
const animalId = new URLSearchParams(window.location.search).get("id");
const currencyFormatter = new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0
});

function showError(message) {
    formMessage.textContent = message;
    formMessage.classList.remove("hidden");
    formMessage.scrollIntoView({ behavior: "smooth", block: "center" });
}

function updateFormattedPrice() {
    formattedPrice.textContent = `Valor mostrado: ${currencyFormatter.format(Number(priceInput.value || 0))}`;
}

function collectFormData() {
    const data = new FormData(form);
    return {
        code: String(data.get("code")).trim().toUpperCase(),
        name: String(data.get("name")).trim(),
        species: String(data.get("species")),
        breed: String(data.get("breed")).trim(),
        birthDate: String(data.get("birthDate") || ""),
        weight: Number(data.get("weight")),
        price: Number(data.get("price")),
        location: String(data.get("location")).trim(),
        status: String(data.get("status"))
    };
}

function validateBusinessRules(animal) {
    if (animal.birthDate && new Date(`${animal.birthDate}T00:00:00`) > new Date()) {
        return "La fecha de nacimiento no puede ser futura.";
    }
    if (!Number.isFinite(animal.weight) || animal.weight <= 0) {
        return "El peso debe ser mayor que cero.";
    }
    if (!Number.isSafeInteger(animal.price) || animal.price <= 0) {
        return "El precio debe ser un valor entero positivo en pesos colombianos.";
    }
    return "";
}

function loadAnimalForEditing() {
    const today = new Date().toISOString().slice(0, 10);
    document.getElementById("birthDate").max = today;
    if (!animalId) return;

    const animal = GanaAnimalStorage.findById(animalId);
    if (!animal) {
        showError("No se encontró el animal solicitado.");
        form.querySelectorAll("input, select, button").forEach((element) => { element.disabled = true; });
        return;
    }

    document.title = "Editar animal | GANA APP";
    document.getElementById("formTitle").textContent = "Editar animal";
    document.getElementById("submitButton").textContent = "Guardar cambios";
    Object.entries(animal).forEach(([key, value]) => {
        const field = form.elements.namedItem(key);
        if (field) field.value = value ?? "";
    });
    updateFormattedPrice();
}

priceInput.addEventListener("input", updateFormattedPrice);

form.addEventListener("submit", (event) => {
    event.preventDefault();
    formMessage.classList.add("hidden");

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const animal = collectFormData();
    const validationError = validateBusinessRules(animal);
    if (validationError) {
        showError(validationError);
        return;
    }

    try {
        if (animalId) {
            GanaAnimalStorage.update(animalId, animal);
            sessionStorage.setItem("ganaApp.lastOperation", "Animal actualizado correctamente.");
        } else {
            GanaAnimalStorage.create(animal);
            sessionStorage.setItem("ganaApp.lastOperation", "Animal registrado correctamente.");
        }
        window.location.href = "index.html";
    } catch (error) {
        showError(error.message);
    }
});

document.addEventListener("DOMContentLoaded", loadAnimalForEditing);
