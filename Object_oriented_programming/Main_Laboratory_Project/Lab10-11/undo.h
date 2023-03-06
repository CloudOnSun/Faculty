#pragma once
#include "domain.h"
#include "repository.h"

class ActiuneUndo
{
public:
	virtual void doUndo() = 0;
	virtual ~ActiuneUndo() = default;
};

class UndoAdauga :public ActiuneUndo
{
	Film filmAdaugat;
	Repository& repository;

public:
	UndoAdauga(Repository& repository, const Film& film) : repository{ repository }, filmAdaugat{ film }{}
	void doUndo() override
	{
		repository.stergeElement(filmAdaugat.get_titlu());
	}
};

class UndoSterge :public ActiuneUndo
{
	Film filmSters;
	Repository& repository;
public:
	UndoSterge(Repository& repository, const Film& film) : repository{ repository }, filmSters{ film }{}
	void doUndo() override
	{
		repository.adaugaElement(filmSters);
	}
};

class UndoModifica :public ActiuneUndo
{
	Film filmModificat;
	Repository& repository;
public:
	UndoModifica(Repository& repository, const Film& film) : repository{ repository }, filmModificat{ film }{}
	void doUndo() override
	{
		repository.modificaElement(filmModificat);
	}
};