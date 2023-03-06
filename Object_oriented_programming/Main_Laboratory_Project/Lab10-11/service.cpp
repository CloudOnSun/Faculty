#include "service.h"
#include <algorithm>
#include <functional>

void Service::adaugaFilm(const string& titlu, const string& gen, const int& an_aparitie, const string& actorPrincipal)
{
	Film film{ titlu, gen, an_aparitie, actorPrincipal };
	film.verificaFilm();
	repository.adaugaElement(film);
	undoActiuni.push_back(std::make_unique<UndoAdauga>(repository, film));
}

const Film& Service::cautaFilm(const string& titlu) const
{
	return repository.cautaElement(titlu);
}

void Service::stergeFilm(const string& titlu)
{
	const Film film = cautaFilm(titlu);
	repository.stergeElement(titlu);
	undoActiuni.push_back(std::make_unique<UndoSterge>(repository, film));
}

void Service::modificaFilm(const string& titlu, const string& gen, const int& an_aparitie, const string& actorPrincipal)
{
	Film film{ titlu, gen, an_aparitie, actorPrincipal };
	film.verificaFilm();
	const Film filmDeModificat = cautaFilm(titlu);
	undoActiuni.push_back(std::make_unique<UndoModifica>(repository, filmDeModificat));
	repository.modificaElement(film);
}

const vector<Film>& Service::getToateFilmele() const
{
	return repository.getToateElemente();
}

const vector<Film> Service::filtrareFilmeTitlu(const string& titlu) const
{
	auto& lista_filme = repository.getToateElemente();
	vector <Film> lista_filtrata;
	std::copy_if(lista_filme.begin(), lista_filme.end(), std::back_inserter(lista_filtrata), [&titlu](const Film& film) {return film.get_titlu() == titlu; });
	return lista_filtrata;
}

const vector<Film> Service::filtrareFilmeAnAparitie(const int& an_aparitie) const
{
	auto& lista_filme = repository.getToateElemente();
	vector <Film> lista_filtrata;
	std::copy_if(lista_filme.begin(), lista_filme.end(), std::back_inserter(lista_filtrata), [&an_aparitie](const Film& film) noexcept {return film.get_an_aparitie() == an_aparitie; });
	return lista_filtrata;
}

const vector<Film> Service::sortareFilme(const string& criteriu_sortare) const
{
	auto lista_filme = repository.getToateElemente();
	if (criteriu_sortare == "titlu")
	{
		//sortareGenerala(lista_filme, comparaDupaTitlu);
		std::sort(lista_filme.begin(), lista_filme.end(), comparaDupaTitlu);
	}
	else if (criteriu_sortare == "gen")
	{
		//sortareGenerala(lista_filme, comparaDupaGen);
		std::sort(lista_filme.begin(), lista_filme.end(), comparaDupaGen);
	}
	else if (criteriu_sortare == "an aparitie")
	{
		//sortareGenerala(lista_filme, comparaDupaAnAparitie);
		std::sort(lista_filme.begin(), lista_filme.end(), comparaDupaAnAparitie);
	}
	else if (criteriu_sortare == "actor principal")
	{
		//sortareGenerala(lista_filme, comparaDupaActorPrincipal);
		std::sort(lista_filme.begin(), lista_filme.end(), comparaDupaActorPrincipal);
	}
	return lista_filme;
}

/*void Service::sortareGenerala(vector<Film>& lista_sortare, bool funcite_sortare(const Film& film1, const Film& film2)) const
{
	for (size_t i = 0; i < lista_sortare.size(); i++)
	{
		for (size_t j = i + 1; j < lista_sortare.size(); j++)
		{
			if (!funcite_sortare(lista_sortare.at(i), lista_sortare.at(j)))
			{
				const auto auxiliar = lista_sortare.at(i);
				lista_sortare.at(i) = lista_sortare.at(j);
				lista_sortare.at(j) = auxiliar;
			}
		}
	}
}*/

void Service::adaugaFilmInCosCumparaturi(const string& titlu)
{
	const auto& film = repository.cautaElement(titlu);
	this->cosDeCumparaturi.adauga_element_in_cos(film);
}

void Service::golesteCosCumparaturi() noexcept
{
	this->cosDeCumparaturi.goleste_cos();
}

const vector<Film>& Service::getCosCumparaturi() noexcept
{
	return this->cosDeCumparaturi.get_toate_elemente_din_cos();
}

size_t Service::getLungimeCosCumparaturi() const noexcept
{
	return this->cosDeCumparaturi.getLungime();
}

void Service::adaugaFilmeRandomInCos(const int lungimeDorita)
{
	cosDeCumparaturi.adaugaFilmeRandom(getToateFilmele(), lungimeDorita);
}

void Service::exportFisierCosCumparaturi(string fileName)
{
	cosDeCumparaturi.exportFisier(fileName);
}

void Service::undo()
{
	if (undoActiuni.empty())
		return;
	undoActiuni.back()->doUndo();
	undoActiuni.pop_back();
}