#pragma once
#include "domain.h"
#include <vector>
#include <ostream>
#include <random>

using std::vector;

class Repository
{
protected:

	vector <Film> lista_stocare;

	bool existaElement(const string titlu) const;

public:

	Repository() = default;

	Repository(const Repository& altRepo) = delete;

	virtual void adaugaElement(const Film& element);

	virtual const Film& stergeElement(const string titlu);

	virtual const Film& cautaElement(const string titlu) const;

	virtual void modificaElement(const Film& element);

	virtual const size_t getLungime() const;

	virtual const vector<Film>& getToateElemente() const;

	virtual ~Repository() = default;

};

class ExceptieRepository
{
	string mesaj_eroare;

public:

	ExceptieRepository(string mesaj) :mesaj_eroare{ mesaj } {}

	string get_mesaj()
	{
		return mesaj_eroare;
	}

	friend std::ostream& operator<<(std::ostream& out, const ExceptieRepository& exceptie);

};

std::ostream& operator<<(std::ostream& out, const ExceptieRepository& exceptie);

class ExceptieRepositoryProabilitate
{

public:
	string mesaj_eroare;

	ExceptieRepositoryProabilitate(string mesaj) :mesaj_eroare{ mesaj } {}

	string get_mesaj()
	{
		return mesaj_eroare;
	}

	friend std::ostream& operator<<(std::ostream& out, const ExceptieRepository& exceptie);

};

std::ostream& operator<<(std::ostream& out, const ExceptieRepositoryProabilitate& exceptie);

class RepositoryProbabilitate : public Repository
{

private:
	float probabilitate;

public:
	RepositoryProbabilitate(float probabilitate) : Repository(), probabilitate{ probabilitate }{};

	RepositoryProbabilitate(const RepositoryProbabilitate& altRepo) = delete;

	void incearca() const
	{
		const float rezultat = static_cast <float> (rand()) / (static_cast <float> (RAND_MAX));
		if (rezultat <= probabilitate)
		{
			throw ExceptieRepositoryProabilitate("A picat...\n");
		}
	}

	void adaugaElement(const Film& element) override
	{
		incearca();
		Repository::adaugaElement(element);
	}

	const Film& stergeElement(const string titlu) override
	{
		incearca();
		return Repository::stergeElement(titlu);
	}

	const Film& cautaElement(const string titlu) const override
	{
		incearca();
		return Repository::cautaElement(titlu);
	}

	void modificaElement(const Film& element) override
	{
		incearca();
		Repository::modificaElement(element);
	}

	const size_t getLungime() const override
	{
		incearca();
		return Repository::getLungime();
	}

	const vector<Film>& getToateElemente() const override
	{
		incearca();
		return Repository::getToateElemente();
	}

	~RepositoryProbabilitate() = default;
};

class FileRepository : public Repository
{
private:
	string fileName;
	void descarcaDinFisier();
	void incarcaInFisier();

public:
	
	FileRepository(const string fileName);

	void adaugaElement(const Film& element) override;

	const Film& stergeElement(const string titlu) override;

	void modificaElement(const Film& element) override;


};
