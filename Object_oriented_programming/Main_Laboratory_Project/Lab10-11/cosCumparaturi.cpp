#include "cosCumparaturi.h"
#include <fstream>


void CosDeCumparaturi::adauga_element_in_cos(const Film& film)
{
	this->lista_cos.push_back(film);
}

void CosDeCumparaturi::goleste_cos() noexcept
{
	this->lista_cos.clear();
}

const vector<Film>& CosDeCumparaturi::get_toate_elemente_din_cos() const noexcept
{
	return this->lista_cos;
}

size_t CosDeCumparaturi::getLungime() const noexcept
{
	return this->lista_cos.size();
}

void CosDeCumparaturi::adaugaFilmeRandom(vector<Film> filmeExistente, const int numarDorit)
{
	std::shuffle(filmeExistente.begin(), filmeExistente.end(), std::default_random_engine(std::random_device{}()));
	const size_t lungime = lista_cos.size();
	while (lista_cos.size() < lungime + numarDorit && filmeExistente.size() > 0)
	{
		lista_cos.push_back(filmeExistente.back());
		filmeExistente.pop_back();
	}
}

void CosDeCumparaturi::exportFisier(string fileName)
{
	std::ofstream out(fileName);
	if (!out.is_open())
	{
		throw ExceptieCosDeCumparaturi("Error open: " + fileName);
	}
	auto& listaCos = get_toate_elemente_din_cos();
	for (const auto& film : listaCos)
	{
		out << film;
	}
	out.close();
}

std::ostream& operator<<(std::ostream& out, const ExceptieCosDeCumparaturi& exceptie) {
	out << exceptie.mesaj_eroare;
	return out;
}

/*void FileCosDeCumparaturi::incarcaDateInFisier()
{
	std::ofstream out(fileName);
	if (!out.is_open())
	{
		throw ExceptieCosDeCumparaturi("Error open: " + fileName);
	}
	auto& listaCos = get_toate_elemente_din_cos();
	for (const auto& film : listaCos)
	{
		out << film;
	}
	out.close();

}

void  FileCosDeCumparaturi::citesteDateDinFisier()
{
	std::ifstream in(fileName);
	if (!in.is_open())
	{
		throw ExceptieCosDeCumparaturi("Error open: " + fileName);
	}
	while (!in.eof())
	{
		Film film{"", "", 0, ""};
		in >> film;
		CosDeCumparaturi::adauga_element_in_cos(film);
	}
	in.close();
}*/

std::ostream& operator<<(std::ostream& stream, const Film& film)
{
	stream << film.get_titlu() + "," + film.get_gen() + "," << film.get_an_aparitie() << "," + film.get_actorPrincipal() << "\n";
	return stream;
}

/*std::istream& operator>>(std::istream& stream, Film& film)
{
	string linie, delimitator=",", buffer;
	size_t pozitieInLinie = 0;
	int contorElemente = 1;
	string titlu, gen, actor_Principal;
	int an_aparitie = 0;

	stream >> linie;

	while ((pozitieInLinie = linie.find(delimitator)) != string::npos)
	{
		buffer = linie.substr(0, pozitieInLinie);
		if (contorElemente == 1)
		{
			titlu = buffer;
		}
		else if(contorElemente == 2)
		{
			gen = buffer;
		}
		else if (contorElemente == 3)
		{
			an_aparitie = stoi(buffer);
		}
		else if (contorElemente == 4)
		{
			actor_Principal = buffer;
		}
		else
		{
			throw ExceptieCosDeCumparaturi("Fisiere corupte!!");
		}
		contorElemente++;
		linie.erase(0, pozitieInLinie + delimitator.length());
		film.set_titlu(titlu);
		film.set_gen(gen);
		film.set_an_aparitie(an_aparitie);
		film.set_actorPrincipal(actor_Principal);
	}
	return stream;
}*/