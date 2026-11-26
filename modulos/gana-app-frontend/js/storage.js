"use strict";

/**
 * Capa de persistencia del prototipo front-end.
 * Centraliza las operaciones CRUD para evitar acceso directo a localStorage
 * desde las pantallas y facilitar una futura sustitución por una API REST.
 */
const GanaAnimalStorage = (() => {
    const STORAGE_KEY = "ganaApp.animals.v1";

    function readAll() {
        try {
            const storedValue = JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]");
            return Array.isArray(storedValue) ? storedValue : [];
        } catch (error) {
            console.error("No fue posible leer el inventario.", error);
            return [];
        }
    }

    function writeAll(animals) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(animals));
    }

    function findById(id) {
        return readAll().find((animal) => animal.id === id) || null;
    }

    function create(animalData) {
        const animals = readAll();
        const duplicatedCode = animals.some(
            (animal) => animal.code.toLowerCase() === animalData.code.toLowerCase()
        );
        if (duplicatedCode) throw new Error("Ya existe un animal con ese código.");

        const animal = {
            ...animalData,
            id: crypto.randomUUID(),
            createdAt: new Date().toISOString()
        };
        animals.push(animal);
        writeAll(animals);
        return animal;
    }

    function update(id, animalData) {
        const animals = readAll();
        const index = animals.findIndex((animal) => animal.id === id);
        if (index < 0) throw new Error("El animal solicitado no existe.");

        const duplicatedCode = animals.some(
            (animal) => animal.id !== id
                && animal.code.toLowerCase() === animalData.code.toLowerCase()
        );
        if (duplicatedCode) throw new Error("Ya existe otro animal con ese código.");

        animals[index] = { ...animals[index], ...animalData, updatedAt: new Date().toISOString() };
        writeAll(animals);
        return animals[index];
    }

    function remove(id) {
        const animals = readAll();
        const filteredAnimals = animals.filter((animal) => animal.id !== id);
        if (filteredAnimals.length === animals.length) return false;
        writeAll(filteredAnimals);
        return true;
    }

    return Object.freeze({ readAll, findById, create, update, remove });
})();
