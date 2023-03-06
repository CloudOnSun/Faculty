#pragma once
#include <qlistwidget.h>
#include <qtablewidget.h>
#include <QAbstractListModel>
#include "basicOperationsWidget.h"
#include "getSortFiltruWidget.h"
#include "cosCumparaturiWidget.h"


class MyListModel : public QAbstractListModel
{

	vector <Film> filme;

public:

	MyListModel(const vector<Film> filme) : filme{ filme } {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return (int)filme.size();
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override
	{
		if (role == Qt::DisplayRole)
		{
			auto film = filme[index.row()];
			return QString::fromStdString(film.toString());
		}
		return QVariant{};
	}


	void setFilme(const vector<Film>& filme)
	{
		this->filme = filme;
		auto topleft = createIndex(0, 0);
		auto bottomRight = createIndex(rowCount(), 1);
		emit dataChanged(topleft, bottomRight);
	}

};


class MainActivityWidget : public QWidget
{
	
private:

	Repository repository;
	FileRepository fileRepository{ "fisierDate.txt" };
	RepositoryProbabilitate repositoryProbabilitate{ (float)1 };
	Service service{ fileRepository };

	//QListWidget* listaFilmeWidget;
	MyListModel* listModel;
	QListView* listView;
	QTableWidget* tabelFilme;
	QPushButton* butonCosCrud;
	QPushButton* butonCosPaint;
	
	BasicOperationsWidget* basicOperationsWidget;
	GetSortFiltruWidget* getSortFiltruWidget;
	ContorCosCumparaturi* contorCos;
	ListaCosCumparaturi* listaCos;
	CosCumparaturiWidget* cosCumparaturiWidget;
	vector <CosCRUDGUI*> listaCosuriCRUD;
	vector <CosCumparaturiObservator* > observatori;

	void deschidCosCRUD();
	void deschidCosReadOnlyGUI();
	void conecteazaListaFilmeCuInputFilme();
	void creeazaConexiuni();
	void updateCasutaFilme(const vector<Film>& listaFilme);
	void adaugaButoaneGenuri(QLayout* layout);
	void getNumarFilmeDupaGen(QPushButton* butonGen);

public:

	MainActivityWidget(QWidget* parent = nullptr);


};
