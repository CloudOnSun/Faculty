#pragma once
#include "domain.h"
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using std::vector;

class CosDeCumparaturi
{
private:
	vector<Film> lista_cos;
public:
	CosDeCumparaturi() = default;

	virtual void adauga_element_in_cos(const Film& film);

	virtual void goleste_cos() noexcept;

	const vector<Film>& get_toate_elemente_din_cos() const noexcept;

	size_t getLungime() const noexcept;

	virtual void adaugaFilmeRandom(vector<Film> filmeExistente, const int numarDorit);

	void exportFisier(string fileName);

	virtual ~CosDeCumparaturi() = default;

	friend std::ostream& operator<<(std::ostream& stream, const Film& film);
};

/*class FileCosDeCumparaturi : public CosDeCumparaturi
{
private:
	string fileName;
	void incarcaDateInFisier();
	void citesteDateDinFisier();

public:

	FileCosDeCumparaturi(string fileName) : CosDeCumparaturi(), fileName{ fileName }
	{
		citesteDateDinFisier();
	}

	void adauga_element_in_cos(const Film& film) override
	{
		CosDeCumparaturi::adauga_element_in_cos(film);
		incarcaDateInFisier();
	}

	void goleste_cos() override
	{
		CosDeCumparaturi::goleste_cos();
		incarcaDateInFisier();
	}

	void adaugaFilmeRandom(vector<Film> filmeExistente, const int numarDorit) override
	{
		CosDeCumparaturi::adaugaFilmeRandom(filmeExistente, numarDorit);
		incarcaDateInFisier();
	}

friend std::ostream& operator<<(std::ostream& stream, const Film& film);

friend std::istream& operator>>(std::istream& stream, Film& film);

};
*/
std::ostream& operator<<(std::ostream& stream, const Film& film);

//std::istream& operator>>(std::istream& stream, Film& film);

class ExceptieCosDeCumparaturi
{
	string mesaj_eroare;

public:

	ExceptieCosDeCumparaturi(string mesaj) :mesaj_eroare{ mesaj } {}

	friend std::ostream& operator<<(std::ostream& out, const ExceptieCosDeCumparaturi& exceptie);

};

std::ostream& operator<<(std::ostream& out, const ExceptieCosDeCumparaturi& exceptie);