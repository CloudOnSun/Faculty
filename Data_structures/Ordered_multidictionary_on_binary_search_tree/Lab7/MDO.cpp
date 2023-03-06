#include "IteratorMDO.h"
#include "MDO.h"
#include <iostream>
#include <vector>
#include <queue>

#include <exception>
using namespace std;


void MDO::redimensionare()
{
	TElem* nouElemente = new TElem[2 * capacitate];
	int* nouStanga = new int[2 * capacitate];
	int* nouDreapta = new int[2 * capacitate];
	for (int i = 0; i < capacitate; i++)
	{
		nouElemente[i] = elemente[i];
		nouStanga[i] = stanga[i];
		nouDreapta[i] = dreapta[i];
	}
	//delete[] urmator
	for (int i = capacitate; i < 2 * capacitate-1; i++)
	{
		nouStanga[i] = i + 1;
	}
	nouStanga[2 * capacitate - 1] = -1;
	primLiber = capacitate;
	capacitate = capacitate * 2;
	delete[] elemente;
	elemente = nouElemente;
	delete[] stanga;
	stanga = nouStanga;
	delete[] dreapta;
	dreapta = nouDreapta;
}

void MDO::initSpatiuLiber()
{
	for (int i = 0; i < capacitate - 1; i++)
	{
		stanga[i] = i + 1;
	}
	stanga[capacitate - 1] = -1;
	primLiber = 0;
}

int MDO::alocaNod()
{
	int nod = primLiber;
	primLiber = stanga[primLiber];
	return nod;
}





MDO::MDO(Relatie r) {
	/* de adaugat */
	this->r = r;
	this->capacitate = 8;
	elemente = new TElem[capacitate];
	stanga = new int[capacitate];
	dreapta = new int[capacitate];

	this->rad = -1;
	initSpatiuLiber();

}


void MDO::adauga(TCheie c, TValoare v) {
	/* de adaugat */

	TElem e = TElem(c, v);

	if (primLiber == -1)
	{
		redimensionare();
	}

	int pozitie = alocaNod();
	elemente[pozitie] = e;
	stanga[pozitie] = -1;
	dreapta[pozitie] = -1;
	
	if(rad != -1)
	{
		int cautare = rad;
		int anterior = -1;
		while (cautare != -1)
		{
			anterior = cautare;
			if (r(elemente[cautare].first, c))
			{
				cautare = stanga[cautare];
			}
			else
			{
				cautare = dreapta[cautare];
			}
		}
		if (r(elemente[anterior].first, c))
		{
			stanga[anterior] = pozitie;
		}
		else
		{
			dreapta[anterior] = pozitie;
		}

	}
	else
	{
		rad = pozitie;
	}
}

vector<TValoare> MDO::cauta(TCheie c) const {
	/* de adaugat */
	vector<TValoare> v;
	int cautare = rad;
	while (cautare != -1)
	{
		if (elemente[cautare].first == c)
		{
			v.push_back(elemente[cautare].second);
		}
		if (r(elemente[cautare].first, c))
		{
			cautare = stanga[cautare];
		}
		else
		{
			cautare = dreapta[cautare];
		}
	}
	return v;
}

bool MDO::sterge(TCheie c, TValoare v) {
	/* de adaugat */

	int cautare = rad;
	int anterior = -1;
	while (elemente[cautare].first != c)
	{
		anterior = cautare;
		if (r(elemente[cautare].first, c))
		{
			cautare = stanga[cautare];
		}
		else
		{
			cautare = dreapta[cautare];
		}
	}
	if (cautare != rad)
	{
		// daca stergem nodul stang al celui anterior
		if (stanga[anterior] == cautare)
		{
			//daca nodul de sters nu are arbore stang
			if (stanga[cautare] == -1 && dreapta[cautare] != -1)
			{
				stanga[anterior] = dreapta[cautare];
			}
			// daca nodul de sters nu are arobore drept
			if (stanga[cautare] != -1 && dreapta[cautare] == -1)
			{
				stanga[anterior] = stanga[cautare];
			}
			//daca nodul sters e frunza
			if (stanga[cautare] == -1 && dreapta[cautare] == -1)
			{
				stanga[anterior] = -1;
			}
			stanga[cautare] = primLiber;
			primLiber = cautare;
		}
		else if (dreapta[anterior] == cautare)
		{
			//daca nodul de sters nu are arbore stang
			if (stanga[cautare] == -1 && dreapta[cautare] != -1)
			{
				dreapta[anterior] = dreapta[cautare];
			}
			// daca nodul de sters nu are arobore drept
			if (stanga[cautare] != -1 && dreapta[cautare] == -1)
			{
				dreapta[anterior] = stanga[cautare];
			}
			//daca nodul sters e frunza
			if (stanga[cautare] == -1 && dreapta[cautare] == -1)
			{
				dreapta[anterior] = -1;
			}
			stanga[cautare] = primLiber;
			primLiber = cautare;
		}

	}
	else
	{
		// daca nodul de sters nu are arbore stang
		if (stanga[cautare] == -1 && dreapta[cautare] != -1)
		{
			rad = dreapta[cautare];
		}
		// daca nodul de sters nu are arobore drept
		if (stanga[cautare] != -1 && dreapta[cautare] == -1)
		{
			rad = stanga[cautare];
		}
		if (stanga[cautare] == -1 && dreapta[cautare] == -1)
		{
			rad = -1;
		}
		stanga[cautare] = primLiber;
		primLiber = cautare;
	}
	

	return false;
}

int MDO::dim() const {
	/* de adaugat */
	queue <int> q;
	if (rad != -1)
	{
		q.push(rad);
		int nr = 0;
		while (!q.empty())
		{
			int nod = q.front();
			q.pop();
			nr++;
			if (stanga[nod] != -1)
			{
				q.push(stanga[nod]);
			}
			if (dreapta[nod] != -1)
			{
				q.push(dreapta[nod]);
			}
		}
		return nr;
	}
	
	return 0;
}

bool MDO::vid() const {
	/* de adaugat */
	if (rad == -1)
		return true;
	else
		return false;
}

IteratorMDO MDO::iterator() const {
	return IteratorMDO(*this);
}

MDO::~MDO() {
	/* de adaugat */
	delete[] elemente;
	delete[] stanga;
	delete[] dreapta;
}
