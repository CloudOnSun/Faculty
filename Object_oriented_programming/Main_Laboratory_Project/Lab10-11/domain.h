#pragma once
#include <string>
#include <iostream>
using std::string;
class Film
{
private:

	string titlu;
	string gen;
	int an_aparitie;
	string actorPrincipal;

public:

	Film(const string& titlu, const string& gen, const int& an_aparitie, const string& actorPrincipal);

	Film(const Film& altFilm) :titlu{ altFilm.titlu }, gen{ altFilm.gen }, an_aparitie{ altFilm.an_aparitie }, actorPrincipal{ altFilm.actorPrincipal } {}

	Film& operator=(const Film& altFilm)
	{
		this->titlu = altFilm.titlu;
		this->gen = altFilm.gen;
		this->an_aparitie = altFilm.an_aparitie;
		this->actorPrincipal = altFilm.actorPrincipal;
		return (*this);
	}

	string get_titlu() const { return this->titlu; }

	string get_gen() const { return this->gen; }

	int get_an_aparitie() const noexcept { return this->an_aparitie; }

	string get_actorPrincipal() const { return this->actorPrincipal; }

	void set_titlu(string titlu1) { this->titlu = titlu1; }

	void set_gen(string gen1) { this->gen = gen1; }

	void set_an_aparitie(int an_aparitie1) noexcept { this->an_aparitie = an_aparitie1; }

	void set_actorPrincipal(string actor_Principal1) { this->actorPrincipal = actor_Principal1; }

	bool verificaFilm() const;

	bool operator ==(const Film& altFilm) const;

	string toString() const { return this->titlu + "," + this->gen + "," + std::to_string(this->an_aparitie) + "," + this->actorPrincipal; }

};

bool comparaDupaTitlu(const Film& film1, const Film& film2);

bool comparaDupaGen(const Film& film1, const Film& film2);

bool comparaDupaAnAparitie(const Film& film1, const Film& film2) noexcept;

bool comparaDupaActorPrincipal(const Film& film1, const Film& film2);

class ExceptieValidare
{
	string mesaj_eroare;

public:

	ExceptieValidare(string mesaj) :mesaj_eroare{ mesaj } {}

	string get_mesaj()
	{
		return mesaj_eroare;
	}

	friend std::ostream& operator<<(std::ostream& out, const ExceptieValidare& exceptie);

};

std::ostream& operator<<(std::ostream& out, const ExceptieValidare& exceptie);