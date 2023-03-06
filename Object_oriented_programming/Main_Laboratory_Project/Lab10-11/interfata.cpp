#include "interfata.h"
#include <iostream>
using std::cout;
using std::cin;
using std::getline;

void InterfataUtilizator::adauga_film()
{
	string titlu, gen, actorPrincipal, buffer;
	int an_aparitie = 0;
	cout << "Cititi titlul filmului: ";
	getline(cin, titlu, '\n');
	cout << "Cititi genul filmului: ";
	getline(cin, gen, '\n');
	cout << "Cititi anul de aparitie al filmului: ";
	cin >> an_aparitie;
	getline(cin, buffer, '\n');
	cout << "Cititi actorul principal al filmului: ";
	getline(cin, actorPrincipal, '\n');
	try
	{
		service.adaugaFilm(titlu, gen, an_aparitie, actorPrincipal);
		cout << "Film adaugat cu succes\n";
	}
	catch (const ExceptieRepository& exceptie)
	{
		cout << exceptie << "\n";
	}
	catch (const ExceptieValidare& exceptie)
	{
		cout << exceptie << "\n";
	}
	catch (const ExceptieRepositoryProabilitate& exceptie)
	{
		cout << exceptie << "\n";
	}

}

void InterfataUtilizator::sterge_film()
{
	string titlu;
	cout << "Cititi titlul filmului: ";
	getline(cin, titlu, '\n');
	try
	{
		service.stergeFilm(titlu);
		cout << "Film sters cu succes\n";
	}
	catch (const ExceptieRepository& exceptie)
	{
		cout << exceptie << "\n";
	}
	catch (const ExceptieRepositoryProabilitate& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::cauta_film()
{
	string titlu;
	cout << "Cititi titlul filmului: ";
	getline(cin, titlu, '\n');
	try
	{
		const auto& film = service.cautaFilm(titlu);
		cout << film.get_titlu() + " -- " + film.get_gen() + " -- " + film.get_actorPrincipal() + " -- " << film.get_an_aparitie() << "\n";
	}
	catch (const ExceptieRepository& exceptie)
	{
		cout << exceptie << "\n";
	}
	catch (const ExceptieRepositoryProabilitate& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::modifica_film()
{
	string titlu, gen, actorPrincipal, buffer;
	int an_aparitie = 0;
	cout << "Cititi titlul filmului: ";
	getline(cin, titlu, '\n');
	cout << "Cititi genul filmului: ";
	getline(cin, gen, '\n');
	cout << "Cititi anul de aparitie al filmului: ";
	cin >> an_aparitie;
	getline(cin, buffer, '\n');
	cout << "Cititi actorul principal al filmului: ";
	getline(cin, actorPrincipal, '\n');
	try
	{
		service.modificaFilm(titlu, gen, an_aparitie, actorPrincipal);
		cout << "Film modifiat cu succes\n";
	}
	catch (const ExceptieRepository& exceptie)
	{
		cout << exceptie << "\n";
	}
	catch (const ExceptieValidare& exceptie)
	{
		cout << exceptie << "\n";
	}
	catch (const ExceptieRepositoryProabilitate& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::afiseaza_toate_filmele()
{
	try
	{
		const auto& lista_filme = service.getToateFilmele();
		for (const auto& film : lista_filme)
		{
			if (&film != nullptr)
			{
				cout << film.get_titlu() + " -- " + film.get_gen() + " -- " + film.get_actorPrincipal() + " -- " << film.get_an_aparitie() << "\n";

			}
		}
	}
	catch (const ExceptieRepositoryProabilitate& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::filtreaza_filmele()
{
	try
	{
		string criteriuFiltrare, titlu, buffer;
		int anAparitie = -1;
		cout << "Cititi criteriul de filtrare (titlu sau an aparitie): ";
		getline(cin, criteriuFiltrare, '\n');
		if (criteriuFiltrare == "titlu")
		{
			cout << "Cititi titlul filmului: ";
			getline(cin, titlu, '\n');
			const auto& lista_filme = service.filtrareFilmeTitlu(titlu);
			for (const auto& film : lista_filme)
			{
				if (&film != nullptr)
				{
					cout << film.get_titlu() + " -- " + film.get_gen() + " -- " + film.get_actorPrincipal() + " -- " << film.get_an_aparitie() << "\n";

				}
			}
		}
		else if (criteriuFiltrare == "an aparitie")
		{
			cout << "Cititi anul de aparitie al filmului: ";
			cin >> anAparitie;
			getline(cin, buffer, '\n');
			const auto& lista_filme = service.filtrareFilmeAnAparitie(anAparitie);
			for (const auto& film : lista_filme)
			{
				if (&film != nullptr)
				{
					cout << film.get_titlu() + " -- " + film.get_gen() + " -- " + film.get_actorPrincipal() + " -- " << film.get_an_aparitie() << "\n";

				}
			}
		}
	}
	catch (const ExceptieRepositoryProabilitate& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::sorteaza_filmele()
{
	try
	{
		string criteriuSortare;
		cout << "Cititi criteriul de sortare (titlu/gen/an aparitie/actor principal): ";
		getline(cin, criteriuSortare, '\n');
		const auto& lista_filme = service.sortareFilme(criteriuSortare);
		for (const auto& film : lista_filme)
		{
			if (&film != nullptr)
			{
				cout << film.get_titlu() + " -- " + film.get_gen() + " -- " + film.get_actorPrincipal() + " -- " << film.get_an_aparitie() << "\n";

			}
		}
	}
	catch (const ExceptieRepositoryProabilitate& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::adauga_film_cos_cumparaturi()
{
	string titlu;
	cout << "Cititi titlul filmului: ";
	getline(cin, titlu, '\n');
	try
	{
		service.adaugaFilmInCosCumparaturi(titlu);
		cout << "Marimea cosului este: " << service.getLungimeCosCumparaturi() << "\n";
	}
	catch (const ExceptieRepository& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::goleste_cos_cumparaturi()
{
	service.golesteCosCumparaturi();
	cout << "Marimea cosului este: " << service.getLungimeCosCumparaturi() << "\n";
}

void InterfataUtilizator::afiseaza_cos_cumparaturi()
{
	const auto& lista_filme = service.getCosCumparaturi();
	for (const auto& film : lista_filme)
	{
		if (&film != nullptr)
		{
			cout << film.get_titlu() + " -- " + film.get_gen() + " -- " + film.get_actorPrincipal() + " -- " << film.get_an_aparitie() << "\n";
		}
	}
}

void InterfataUtilizator::adauga_filme_random_cos_cumparaturi()
{
	int lungimeDorita;
	string buffer;
	cin >> lungimeDorita;
	getline(cin, buffer, '\n');
	service.adaugaFilmeRandomInCos(lungimeDorita);
	cout << "Marimea cosului este: " << service.getLungimeCosCumparaturi() << "\n";
}

void InterfataUtilizator::export_in_fisier()
{
	string fileName;
	cout << "Cititi numele fisierului: ";
	getline(cin, fileName, '\n');
	try
	{
		service.exportFisierCosCumparaturi(fileName);
		cout << "Cos exportat cu succes\n";
	}
	catch (const ExceptieCosDeCumparaturi& exceptie)
	{
		cout << exceptie << "\n";
	}
}

void InterfataUtilizator::undo()
{
	service.undo();
	cout << "Undo realizat cu succes\n";
}

void InterfataUtilizator::afiseaza_meniu()
{
	cout << "0 Iesire aplicatie\n";
	cout << "1 Adauga film\n";
	cout << "2 Sterge film\n";
	cout << "3 Cauta film\n";
	cout << "4 Modifica film\n";
	cout << "5 Afiseaza toate filmele\n";
	cout << "6 Filtreaza filme\n";
	cout << "7 Sorteaza filmele\n";
	cout << "8 Intrare meniu cos de cumparaturi\n";
	cout << "9 Undo\n";
}

void InterfataUtilizator::afiseaza_meniu_cos_cumparturi()
{
	cout << "0 Iesire meniu cos\n";
	cout << "1 Adauga film in cos\n";
	cout << "2 Goleste cosul\n";
	cout << "3 Afiseaza filmele din cos\n";
	cout << "4 Adauga un numar de filme random in cos\n";
	cout << "5 Export in fisier\n";
}


void InterfataUtilizator::runMeniuCos()
{
	string comanda;
	while (true)
	{
		afiseaza_meniu_cos_cumparturi();
		getline(cin, comanda, '\n');
		if (comanda == "0")
		{
			break;
		}
		else if (comanda == "1")
		{
			adauga_film_cos_cumparaturi();
		}
		else if (comanda == "2")
		{
			goleste_cos_cumparaturi();
		}
		else if (comanda == "3")
		{
			afiseaza_cos_cumparaturi();
		}
		else if (comanda == "4")
		{
			adauga_filme_random_cos_cumparaturi();
		}
		else if (comanda == "5")
		{
			export_in_fisier();
		}
		else
		{
			cout << "Comanda invalida\n";
		}
	}
}

void InterfataUtilizator::runUI()
{
	string comanda;
	while (true)
	{
		afiseaza_meniu();
		getline(cin, comanda, '\n');
		if (comanda == "0")
		{
			break;
		}
		else if (comanda == "1")
		{
			adauga_film();
		}
		else if (comanda == "2")
		{
			sterge_film();
		}
		else if (comanda == "3")
		{
			cauta_film();
		}
		else if (comanda == "4")
		{
			modifica_film();
		}
		else if (comanda == "5")
		{
			afiseaza_toate_filmele();
		}
		else if (comanda == "6")
		{
			filtreaza_filmele();
		}
		else if (comanda == "7")
		{
			sorteaza_filmele();
		}
		else if (comanda == "8")
		{
			runMeniuCos();
		}
		else if (comanda == "9")
		{
			undo();
		}
		else
		{
			cout << "Comanda invalida\n";
		}

	}
}