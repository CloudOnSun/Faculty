#include "repository.h"
#include <sstream>
#include <algorithm>
#include <fstream>

void Repository::adaugaElement(const Film& element)
{
	if (existaElement(element.get_titlu()))
	{
		throw ExceptieRepository("Exista deja film cu titlul: " + element.get_titlu());
	}
	lista_stocare.push_back(element);
}

bool Repository::existaElement(const string titlu) const
{
	for (const auto& film : lista_stocare)
	{
		if (&film != nullptr)
		{
			if (titlu == film.get_titlu())
			{
				return true;
			}
		}
	}
	return false;
}

const size_t Repository::getLungime() const
{
	return lista_stocare.size();
}


const Film& Repository::cautaElement(const string titlu) const
{
	/*for (const auto& film : lista_stocare)
	{
		if (&film != nullptr)
		{
			if (titlu == film.get_titlu())
			{
				return film;
			}
		}
	}*/
	vector<Film>::const_iterator pointerFilm = std::find_if(this->lista_stocare.begin(), this->lista_stocare.end(), [=](const Film& film) {return film.get_titlu() == titlu; });

	if (pointerFilm != this->lista_stocare.end())
	{
		return (*pointerFilm);
	}
	else
	{
		throw ExceptieRepository("Nu exista film cu titlul: " + titlu);
	}
}


const Film& Repository::stergeElement(const string titlu)
{
	int i = 0;
	for (const auto& film : lista_stocare)
	{
		if (&film != nullptr)
		{
			if (titlu == film.get_titlu())
			{
				lista_stocare.erase(lista_stocare.begin() + i);
				return film;
			}
			i++;
		}
	}
	throw ExceptieRepository("Nu exista film cu titlul: " + titlu);
}

void Repository::modificaElement(const Film& element)
{
	bool gasit = false;
	for (auto& film : lista_stocare)
	{
		if (&film != nullptr)
		{
			if (element.get_titlu() == film.get_titlu())
			{
				film = element;
				gasit = true;
				break;
			}
		}
	}
	if (!gasit)
	{
		throw ExceptieRepository("Nu exista film cu titlul: " + element.get_titlu());
	}
}

const vector<Film>& Repository::getToateElemente() const
{
	return lista_stocare;
}

std::ostream& operator<<(std::ostream& out, const ExceptieRepository& exceptie) {
	out << exceptie.mesaj_eroare;
	return out;
}

std::ostream& operator<<(std::ostream& out, const ExceptieRepositoryProabilitate& exceptie) {
	out << exceptie.mesaj_eroare;
	return out;
}

void FileRepository::descarcaDinFisier()
{
	std::ifstream fisier(fileName);
	if (!fisier.is_open())
	{
		throw ExceptieRepository("Nu s-a putut deschide fisierul pentru citire!");
	}
	string titlu, gen, actorPrincipal;
	int anAparitie;
	string linie;
	while (std::getline(fisier, linie))
	{
		size_t pozitieDelimitator = linie.find_first_of(",");
		titlu = linie.substr(0, pozitieDelimitator);
		linie = linie.substr(pozitieDelimitator + 1, linie.length());

		pozitieDelimitator = linie.find_first_of(",");
		gen = linie.substr(0, pozitieDelimitator);
		linie = linie.substr(pozitieDelimitator + 1, linie.length());

		pozitieDelimitator = linie.find_first_of(",");
		anAparitie = stoi(linie.substr(0, pozitieDelimitator));
		linie = linie.substr(pozitieDelimitator + 1, linie.length());

		pozitieDelimitator = linie.find_first_of(",");
		actorPrincipal = linie.substr(0, pozitieDelimitator);
		linie = linie.substr(pozitieDelimitator + 1, linie.length());

		Film film{ titlu, gen, anAparitie, actorPrincipal };

		Repository::adaugaElement(film);
	}
	fisier.close();

}

void FileRepository::incarcaInFisier()
{
	std::ofstream fisier(fileName);
	if (!fisier.is_open())
	{
		throw ExceptieRepository("Fisierul nu s-a putut deschide pentru scriere!");
	}
	for (const auto& film : lista_stocare)
	{
		fisier << film.toString() << std::endl;
	}
	fisier.close();
}

FileRepository::FileRepository(const string fileName) : Repository(), fileName{ fileName }
{
	descarcaDinFisier();
}

void FileRepository::adaugaElement(const Film& element)
{
	Repository::adaugaElement(element);
	incarcaInFisier();
}

const Film& FileRepository::stergeElement(const string titlu)
{
	const auto& film = Repository::stergeElement(titlu);
	incarcaInFisier();
	return film;
}

void FileRepository::modificaElement(const Film& element)
{
	Repository::modificaElement(element);
	incarcaInFisier();
}
