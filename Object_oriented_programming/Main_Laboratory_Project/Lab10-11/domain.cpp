#include "domain.h"
#include <sstream>

Film::Film(const string& titlu, const string& gen, const int& an_aparitie, const string& actorPrincipal)
{
	this->titlu = titlu;
	this->gen = gen;
	this->an_aparitie = an_aparitie;
	this->actorPrincipal = actorPrincipal;
}

bool Film::operator ==(const Film& altFilm) const
{
	return titlu == altFilm.get_titlu();
}

bool Film::verificaFilm() const
{
	if (titlu == "")
	{
		throw ExceptieValidare("Titlu invalid");
	}
	if (gen == "")
	{
		throw ExceptieValidare("Gen invalid");
	}
	if (an_aparitie < 1900 || an_aparitie>2022)
	{
		throw ExceptieValidare("An aparitie invalid");
	}
	if (actorPrincipal == "")
	{
		throw ExceptieValidare("Actor principal invalid");
	}
	return true;
}

bool comparaDupaTitlu(const Film& film1, const Film& film2)
{
	return film1.get_titlu() < film2.get_titlu();
}

bool comparaDupaGen(const Film& film1, const Film& film2)
{
	return film1.get_gen() < film2.get_gen();
}

bool comparaDupaAnAparitie(const Film& film1, const Film& film2) noexcept
{
	return film1.get_an_aparitie() < film2.get_an_aparitie();
}

bool comparaDupaActorPrincipal(const Film& film1, const Film& film2)
{
	return film1.get_actorPrincipal() < film2.get_actorPrincipal();
}

std::ostream& operator<<(std::ostream& out, const ExceptieValidare& exceptie) {
	out << exceptie.mesaj_eroare;
	return out;
}