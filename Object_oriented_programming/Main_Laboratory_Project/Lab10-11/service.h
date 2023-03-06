#pragma once
#include "repository.h"
#include "cosCumparaturi.h"
#include "Undo.h"
#include <memory>

class Service
{
private:

	Repository& repository;
	CosDeCumparaturi cosDeCumparaturi;
	vector<std::unique_ptr<ActiuneUndo>> undoActiuni;
	void sortareGenerala(vector<Film>& lista_sortare, bool funcite_sortare(const Film& film1, const Film& film2)) const;

public:

	Service(Repository& repository) noexcept :repository{ repository } {}

	Service(const Service& altSerivce) = delete;

	void adaugaFilm(const string& titlu, const string& gen, const int& an_aparitie, const string& actorPrincipal);

	void stergeFilm(const string& titlu);

	void modificaFilm(const string& titlu, const string& gen, const int& an_aparitie, const string& actorPrincipal);

	const Film& cautaFilm(const string& titlu) const;

	const vector<Film>& getToateFilmele() const;

	const vector<Film> filtrareFilmeTitlu(const string& titlu) const;

	const vector<Film> filtrareFilmeAnAparitie(const int& an_aparitie) const;

	const vector<Film> sortareFilme(const string& criteriu_sortare) const;

	void adaugaFilmInCosCumparaturi(const string& titlu);

	void golesteCosCumparaturi() noexcept;

	const vector<Film>& getCosCumparaturi() noexcept;

	size_t getLungimeCosCumparaturi() const noexcept;

	void adaugaFilmeRandomInCos(const int lungimeDorita);

	void exportFisierCosCumparaturi(string fileName);

	void undo();
};