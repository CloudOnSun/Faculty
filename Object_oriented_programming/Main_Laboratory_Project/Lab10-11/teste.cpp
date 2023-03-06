#include "teste.h"
#include <assert.h>
#include <sstream>
#include <ostream>
void test_creeare_film()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	const Film film(titlu, gen, an_aparitie, actorPrincipal);
	assert(film.get_titlu() == titlu);
	assert(film.get_gen() == gen);
	assert(film.get_an_aparitie() == an_aparitie);
	assert(film.get_actorPrincipal() == actorPrincipal);
	const auto& film2 = film;
	assert(film2.get_titlu() == titlu);
	assert(film2.get_gen() == gen);
	assert(film2.get_an_aparitie() == an_aparitie);
	assert(film2.get_actorPrincipal() == actorPrincipal);

}

void test_validare_film()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	const Film film(titlu, gen, an_aparitie, actorPrincipal);

	assert(film.verificaFilm());

	string titlu2{ "" }, gen2{ "" }, actorPrincipal2{ "" };
	constexpr int an_aparitie2{ 2023 };

	const Film film2(titlu2, gen, an_aparitie, actorPrincipal);
	try
	{
		film2.verificaFilm();
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Titlu invalid") >= 0);
		assert(true);
	}

	const Film film3(titlu, gen2, an_aparitie, actorPrincipal);
	try
	{
		film3.verificaFilm();
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Gen invalid") >= 0);
		assert(true);
	}

	const Film film4(titlu, gen, an_aparitie2, actorPrincipal);
	try
	{
		film4.verificaFilm();
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("An aparitie invalid") >= 0);
		assert(true);
	}

	const Film film5(titlu, gen, an_aparitie, actorPrincipal2);
	try
	{
		film5.verificaFilm();
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Actor principal invalid") >= 0);
		assert(true);
	}
}


void test_adaugare_cautare_repo()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	const Film film(titlu, gen, an_aparitie, actorPrincipal);
	Repository repository;
	repository.adaugaElement(film);
	const auto& film2 = repository.cautaElement(titlu);
	assert(film2.get_titlu() == titlu);
	assert(film2.get_gen() == gen);
	assert(film2.get_an_aparitie() == an_aparitie);
	assert(film2.get_actorPrincipal() == actorPrincipal);
	try
	{
		repository.adaugaElement(film);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Exista deja film cu titlul: Inception") >= 0);
		assert(true);
	}
	try
	{
		const auto film3 = repository.cautaElement("Ziua Z");
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Ziua Z") >= 0);
		assert(true);
	}
}


void test_sterge_repo()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	const Film film(titlu, gen, an_aparitie, actorPrincipal);
	string gen2{ "Actiune" }, actorPrincipal2{ "Tom Hardy" };
	const Film film2("Ziua Z", gen2, an_aparitie, actorPrincipal2);
	Repository repository;
	repository.adaugaElement(film2);
	repository.adaugaElement(film);
	assert(repository.getLungime() == 2);
	repository.stergeElement(titlu);
	assert(repository.getLungime() == 1);
	try
	{
		const auto film3 = repository.cautaElement(titlu);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Inception") >= 0);
		assert(true);
	}
	try
	{
		repository.stergeElement(titlu);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Inception") >= 0);
		assert(true);
	}
}

void test_modifica_repo()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Film film(titlu, gen, an_aparitie, actorPrincipal);
	Repository repository;
	repository.adaugaElement(film);
	string gen2{ "Actiune" }, actorPrincipal2{ "Tom Hardy" };
	Film film2(titlu, gen2, an_aparitie, actorPrincipal2);
	repository.modificaElement(film2);
	const auto& fil = repository.cautaElement(titlu);
	assert(fil.get_titlu() == titlu);
	assert(fil.get_gen() == gen2);
	assert(fil.get_an_aparitie() == an_aparitie);
	assert(fil.get_actorPrincipal() == actorPrincipal2);
	assert(repository.getLungime() == 1);
	Film film3("Ziua Z", gen2, an_aparitie, actorPrincipal2);
	try
	{
		repository.modificaElement(film3);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Ziua Z") >= 0);
		assert(true);
	}
}

void test_getAll_repo()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Film film(titlu, gen, an_aparitie, actorPrincipal);
	Repository repository;
	repository.adaugaElement(film);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	Film film2(titlu2, gen2, an_aparitie2, actorPrincipal2);
	repository.adaugaElement(film2);
	const auto& lista_filme = repository.getToateElemente();
	assert(lista_filme.size() == 2);
	assert(lista_filme.at(0) == film);
	assert(lista_filme.at(1) == film2);
}

void test_adaugare_cautare_service()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	auto& film = service.cautaFilm(titlu);
	assert(film.get_titlu() == titlu);
	assert(film.get_gen() == gen);
	assert(film.get_an_aparitie() == an_aparitie);
	assert(film.get_actorPrincipal() == actorPrincipal);
	try
	{
		service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Exista deja film cu titlul: Inception") >= 0);
		assert(true);
	}
	try
	{
		const auto film3 = service.cautaFilm("Ziua Z");
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Ziua Z") >= 0);
		assert(true);
	}
}

void test_sterge_service()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	service.stergeFilm(titlu);
	try
	{
		const auto film3 = service.cautaFilm(titlu);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Inception") >= 0);
		assert(true);
	}
	try
	{
		service.stergeFilm(titlu);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Inception") >= 0);
		assert(true);
	}
}

void test_modifica_service()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string gen2{ "Actiune" }, actorPrincipal2{ "Tom Hardy" };
	constexpr int an_aparitie2{ 2011 };
	service.modificaFilm(titlu, gen2, an_aparitie2, actorPrincipal2);
	const auto& film = service.cautaFilm(titlu);
	assert(film.get_titlu() == titlu);
	assert(film.get_gen() == gen2);
	assert(film.get_an_aparitie() == an_aparitie2);
	assert(film.get_actorPrincipal() == actorPrincipal2);
	try
	{
		service.modificaFilm("Ziua Z", gen2, an_aparitie, actorPrincipal2);
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: Ziua Z") >= 0);
		assert(true);
	}
	try
	{
		service.modificaFilm("", gen2, an_aparitie, actorPrincipal2);
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Titlu invalid") >= 0);
		assert(true);
	}
	try
	{
		service.modificaFilm("", gen2, an_aparitie, actorPrincipal2);
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Titlu invalid") >= 0);
		assert(true);
	}
	try
	{
		service.modificaFilm("Ziua Z", gen2, an_aparitie, "");
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Actor principal invalid") >= 0);
		assert(true);
	}
	try
	{
		service.modificaFilm("Ziua Z", "", an_aparitie, actorPrincipal2);
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Gen invalid") >= 0);
		assert(true);
	}
	try
	{
		service.modificaFilm("Ziua Z", gen2, 1890, actorPrincipal2);
		assert(false);
	}
	catch (const ExceptieValidare& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("An aparitie invalid") >= 0);
		assert(true);
	}
}

void test_getAll_service()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	const auto& lista_filme = service.getToateFilmele();
	const Film& film = lista_filme.at(0);
	assert(film.get_titlu() == titlu);
	assert(film.get_gen() == gen);
	assert(film.get_an_aparitie() == an_aparitie);
	assert(film.get_actorPrincipal() == actorPrincipal);
	const Film& film2 = lista_filme.at(1);
	assert(film2.get_titlu() == titlu2);
	assert(film2.get_gen() == gen2);
	assert(film2.get_an_aparitie() == an_aparitie2);
	assert(film2.get_actorPrincipal() == actorPrincipal2);
	assert(lista_filme.size() == 2);
}

void test_filtrare_titlu()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	const auto& lista_filtrata = service.filtrareFilmeTitlu("Seven");
	assert(lista_filtrata.size() == 1);
	const auto& film = lista_filtrata.at(0);
	assert(film.get_titlu() == titlu2);
	assert(film.get_gen() == gen2);
	assert(film.get_an_aparitie() == an_aparitie2);
	assert(film.get_actorPrincipal() == actorPrincipal2);

}

void test_filtrare_an_aparitie()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	const auto& lista_filtrata = service.filtrareFilmeAnAparitie(1995);
	assert(lista_filtrata.size() == 1);
	const auto& film = lista_filtrata.at(0);
	assert(film.get_titlu() == titlu2);
	assert(film.get_gen() == gen2);
	assert(film.get_an_aparitie() == an_aparitie2);
	assert(film.get_actorPrincipal() == actorPrincipal2);

}

void test_sortare_titlu()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	const auto& lista_sortata = service.sortareFilme("titlu");
	assert(lista_sortata.size() == 3);

	const auto& film1 = lista_sortata.at(0);
	assert(film1.get_titlu() == titlu3);
	assert(film1.get_gen() == gen3);
	assert(film1.get_an_aparitie() == an_aparitie3);
	assert(film1.get_actorPrincipal() == actorPrincipal3);

	const auto& film2 = lista_sortata.at(1);
	assert(film2.get_titlu() == titlu);
	assert(film2.get_gen() == gen);
	assert(film2.get_an_aparitie() == an_aparitie);
	assert(film2.get_actorPrincipal() == actorPrincipal);

	const auto& film3 = lista_sortata.at(2);
	assert(film3.get_titlu() == titlu2);
	assert(film3.get_gen() == gen2);
	assert(film3.get_an_aparitie() == an_aparitie2);
	assert(film3.get_actorPrincipal() == actorPrincipal2);
}

void test_sortare_gen()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	const auto& lista_sortata = service.sortareFilme("gen");
	assert(lista_sortata.size() == 3);

	const auto& film1 = lista_sortata.at(0);
	assert(film1.get_titlu() == titlu3);
	assert(film1.get_gen() == gen3);
	assert(film1.get_an_aparitie() == an_aparitie3);
	assert(film1.get_actorPrincipal() == actorPrincipal3);

	const auto& film2 = lista_sortata.at(1);
	assert(film2.get_titlu() == titlu);
	assert(film2.get_gen() == gen);
	assert(film2.get_an_aparitie() == an_aparitie);
	assert(film2.get_actorPrincipal() == actorPrincipal);

	const auto& film3 = lista_sortata.at(2);
	assert(film3.get_titlu() == titlu2);
	assert(film3.get_gen() == gen2);
	assert(film3.get_an_aparitie() == an_aparitie2);
	assert(film3.get_actorPrincipal() == actorPrincipal2);
}

void test_sortare_an_aparitie()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	const auto& lista_sortata = service.sortareFilme("an aparitie");
	assert(lista_sortata.size() == 3);

	const auto& film1 = lista_sortata.at(1);
	assert(film1.get_titlu() == titlu3);
	assert(film1.get_gen() == gen3);
	assert(film1.get_an_aparitie() == an_aparitie3);
	assert(film1.get_actorPrincipal() == actorPrincipal3);

	const auto& film2 = lista_sortata.at(2);
	assert(film2.get_titlu() == titlu);
	assert(film2.get_gen() == gen);
	assert(film2.get_an_aparitie() == an_aparitie);
	assert(film2.get_actorPrincipal() == actorPrincipal);

	const auto& film3 = lista_sortata.at(0);
	assert(film3.get_titlu() == titlu2);
	assert(film3.get_gen() == gen2);
	assert(film3.get_an_aparitie() == an_aparitie2);
	assert(film3.get_actorPrincipal() == actorPrincipal2);
}

void test_sortare_actor_principal()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	const auto& lista_sortata = service.sortareFilme("actor principal");
	assert(lista_sortata.size() == 3);

	const auto& film1 = lista_sortata.at(1);
	assert(film1.get_titlu() == titlu3);
	assert(film1.get_gen() == gen3);
	assert(film1.get_an_aparitie() == an_aparitie3);
	assert(film1.get_actorPrincipal() == actorPrincipal3);

	const auto& film2 = lista_sortata.at(2);
	assert(film2.get_titlu() == titlu);
	assert(film2.get_gen() == gen);
	assert(film2.get_an_aparitie() == an_aparitie);
	assert(film2.get_actorPrincipal() == actorPrincipal);

	const auto& film3 = lista_sortata.at(0);
	assert(film3.get_titlu() == titlu2);
	assert(film3.get_gen() == gen2);
	assert(film3.get_an_aparitie() == an_aparitie2);
	assert(film3.get_actorPrincipal() == actorPrincipal2);
}

void test_adauga_in_cos()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	service.adaugaFilmInCosCumparaturi(titlu2);
	auto lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 1);
	service.adaugaFilmInCosCumparaturi(titlu);
	lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 2);
	const auto& lista_filme = service.getCosCumparaturi();
	assert(lista_filme.size() == 2);
	const auto& film1 = lista_filme.at(0);
	assert(film1.get_titlu() == titlu2);
	assert(film1.get_gen() == gen2);
	assert(film1.get_an_aparitie() == an_aparitie2);
	assert(film1.get_actorPrincipal() == actorPrincipal2);
	const auto& film2 = lista_filme.at(1);
	assert(film2.get_titlu() == titlu);
	assert(film2.get_gen() == gen);
	assert(film2.get_an_aparitie() == an_aparitie);
	assert(film2.get_actorPrincipal() == actorPrincipal);

	try
	{
		service.adaugaFilmInCosCumparaturi("aaa");
		assert(false);
	}
	catch (const ExceptieRepository& exceptie)
	{
		std::stringstream outStream;
		outStream << exceptie;
		assert(outStream.str().find("Nu exista film cu titlul: aaa") >= 0);
		assert(true);
	}
}

void test_goleste_cos()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	service.adaugaFilmInCosCumparaturi(titlu2);
	auto lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 1);
	service.adaugaFilmInCosCumparaturi(titlu);
	lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 2);
	service.golesteCosCumparaturi();
	lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 0);
	const auto& lista_filme = service.getCosCumparaturi();
	assert(lista_filme.size() == 0);
}

void test_adauga_random_cos()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	service.adaugaFilmeRandomInCos(1);
	auto lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 1);
	service.adaugaFilmeRandomInCos(2);
	lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 3);
	service.adaugaFilmeRandomInCos(4);
	lungime = service.getLungimeCosCumparaturi();
	assert(lungime == 6);
}

void test_export_fisier_cos()
{
	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	service.adaugaFilmInCosCumparaturi(titlu);
	service.adaugaFilmInCosCumparaturi(titlu2);
	service.adaugaFilmInCosCumparaturi(titlu3);
	service.exportFisierCosCumparaturi("fisier_test.txt");
}

void test_undo()
{

	string titlu{ "Inception" }, gen{ "SF" }, actorPrincipal{ "Leonardo DiCaprio" };
	constexpr int an_aparitie{ 2010 };
	Repository repository;
	Service service{ repository };
	service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
	string titlu2{ "Seven" }, gen2{ "Thriller" }, actorPrincipal2{ "Brad Pitt" };
	constexpr int an_aparitie2{ 1995 };
	service.adaugaFilm(titlu2, gen2, an_aparitie2, actorPrincipal2);
	string titlu3{ "Atonement" }, gen3{ "Drama" }, actorPrincipal3{ "James McAvoy" };
	constexpr int an_aparitie3{ 2003 };
	service.adaugaFilm(titlu3, gen3, an_aparitie3, actorPrincipal3);

	service.adaugaFilmInCosCumparaturi(titlu);
	service.adaugaFilmInCosCumparaturi(titlu2);
	service.adaugaFilmInCosCumparaturi(titlu3);
	service.undo();
	auto& lista_filme = service.getToateFilmele();
	auto& film1 = lista_filme.at(0);
	assert(lista_filme.size() == 2);
	assert(film1.get_titlu() == titlu);
	assert(film1.get_gen() == gen);
	assert(film1.get_an_aparitie() == an_aparitie);
	assert(film1.get_actorPrincipal() == actorPrincipal);
	auto& film2 = lista_filme.at(1);
	assert(film2.get_titlu() == titlu2);
	assert(film2.get_gen() == gen2);
	assert(film2.get_an_aparitie() == an_aparitie2);
	assert(film2.get_actorPrincipal() == actorPrincipal2);

	service.stergeFilm(titlu2);
	assert(lista_filme.size() == 1);
	service.undo();
	auto& lista_filme2 = service.getToateFilmele();
	auto& film3 = lista_filme2.at(0);
	assert(lista_filme2.size() == 2);
	assert(film3.get_titlu() == titlu);
	assert(film3.get_gen() == gen);
	assert(film3.get_an_aparitie() == an_aparitie);
	assert(film3.get_actorPrincipal() == actorPrincipal);
	auto& film4 = lista_filme.at(1);
	assert(film4.get_titlu() == titlu2);
	assert(film4.get_gen() == gen2);
	assert(film4.get_an_aparitie() == an_aparitie2);
	assert(film4.get_actorPrincipal() == actorPrincipal2);

	service.modificaFilm(titlu, gen3, an_aparitie3, actorPrincipal3);
	service.undo();
	auto& film5 = service.cautaFilm(titlu);
	assert(film5.get_titlu() == titlu);
	assert(film5.get_gen() == gen);
	assert(film5.get_an_aparitie() == an_aparitie);
	assert(film5.get_actorPrincipal() == actorPrincipal);
}

void runTeste()
{
	test_validare_film();
	test_creeare_film();
	test_sterge_repo();
	test_adaugare_cautare_repo();
	test_modifica_repo();
	test_getAll_repo();
	test_adaugare_cautare_service();
	test_sterge_service();
	test_modifica_service();
	test_getAll_service();
	test_filtrare_titlu();
	test_filtrare_an_aparitie();
	test_sortare_titlu();
	test_sortare_gen();
	test_sortare_an_aparitie();
	test_sortare_actor_principal();
	test_adauga_in_cos();
	test_goleste_cos();
	test_adauga_random_cos();
	test_export_fisier_cos();
	test_undo();
}