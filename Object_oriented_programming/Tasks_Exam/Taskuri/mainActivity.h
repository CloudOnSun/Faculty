#pragma once
#include <qwidget.h>
#include <qtableview.h>
#include <QAbstractTableModel>
#include "service.h"
#include <qpushbutton.h>
#include <qlineedit.h>
#include <qlistwidget.h>
#include <qboxlayout.h>


class TableModel : public QAbstractTableModel
{
	vector<Task> taskuri;

public:

	TableModel(vector<Task> t) : taskuri {t} {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return taskuri.size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return 4;
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override
	{
		int row = index.row();
		int col = index.column();

		if (role == Qt::DisplayRole)
		{
			Task t = taskuri.at(row);
			if (col == 0)
			{
				return QString::number(t.getId());
			}
			if (col == 1)
			{
				return QString::fromStdString(t.getDesc());
			}
			if (col == 2)
			{
				return QString::fromStdString(t.getStare());
			}
			if (col == 3)
			{
				return QString::fromStdString(t.getPro2());
			}
		}
		return QVariant();
	}

	void setTask(vector <Task> t)
	{
		taskuri = t;
		auto topLeft = createIndex(0, 0);
		auto botRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, botRight);
		emit layoutChanged();
	}


	Qt::ItemFlags flags(const QModelIndex&) const
	{
		return Qt::ItemIsSelectable | Qt::ItemIsEditable | Qt::ItemIsEnabled;
	}

	QVariant headerData(int section, Qt::Orientation ori, int role) const
	{
		if (role == Qt::DisplayRole)
		{
			if (ori == Qt::Horizontal)
			{
				switch (section)
				{
				case 0:
					return tr("ID");
				case 1:
					return tr("Descriere");
				case 2:
					return tr("Stare");
				case 3:
					return tr("Programatori");
				default:
					return QString("");
				}

			}
		}
		return QVariant();
	}
};


class Observer : public QWidget
{

public:

	virtual void update() = 0;

	Observer(QWidget* parent = nullptr) : QWidget(parent) {}


};

inline void notifyObserver(Observer* obs)
{
	obs->update();
}


class obsStare : public Observer
{
private:

	vector<Observer*> obs;
	string stare;
	SrvTask& srv;
	QListWidget* list = new QListWidget;
	QPushButton* closed = new QPushButton("Closed");
	QPushButton* inprogress = new QPushButton("Inprogress");
	QPushButton* open = new QPushButton("Open");


public:

	obsStare(string s, SrvTask& srv) : stare{ s }, srv{ srv } {
		
		QVBoxLayout* vLay = new QVBoxLayout;
		vLay->addWidget(closed);
		vLay->addWidget(inprogress);
		vLay->addWidget(open);
		QWidget* op = new QWidget;
		op->setLayout(vLay);

		QHBoxLayout* hLay = new QHBoxLayout;
		hLay->addWidget(list);
		hLay->addWidget(op);
		this->setLayout(hLay);

		obs.push_back(this);
		connect();
		update();

	}

	void update() override
	{
		list->clear();
		auto v = srv.getAll();
		for (auto t : v)
		{
			if (t.getStare() == stare)
			{
				list->addItem(QString::fromStdString(t.toString()));
			}
		}
	}

	void connect()
	{
		QObject::connect(closed, &QPushButton::clicked, [this]() {
			
			auto v = list->selectedItems();
			if (v.isEmpty()) {
				return;
			}
			auto item = list->selectedItems().at(0);
			auto linie = item->text().toStdString();
			size_t poz = linie.find_first_of(",");
			int id = stoi(linie.substr(0, poz));
			srv.update(id, "closed");
			for (auto each : obs)
			{
				notifyObserver(each);
			}

			});

		QObject::connect(open, &QPushButton::clicked, [&]() {
			auto v = list->selectedItems();
			if (v.isEmpty()) {
				return;
			}
			auto item = list->selectedItems().at(0);
			auto linie = item->text().toStdString();
			size_t poz = linie.find_first_of(",");
			int id = stoi(linie.substr(0, poz));
			srv.update(id, "open");
			for (auto each : obs)
			{
				notifyObserver(each);
			}

			});

		QObject::connect(inprogress, &QPushButton::clicked, [&]() {
			auto v = list->selectedItems();
			if (v.isEmpty()) {
				return;
			}
			auto item = list->selectedItems().at(0);
			auto linie = item->text().toStdString();
			size_t poz = linie.find_first_of(",");
			int id = stoi(linie.substr(0, poz));
			srv.update(id, "inprogress");
			for (auto each : obs)
			{
				notifyObserver(each);
			}

			});
	}

	void adaugaObs(Observer* obs)
	{
		this->obs.push_back(obs);
	}
};


class MainActivity : public Observer
{
	Q_OBJECT

private:

	RepoTask repo{ "fisierDate.txt" };
	SrvTask srv{ repo };

	QTableView* tableView;
	TableModel* tableModel;

	QLineEdit* linieId;
	QLineEdit* linieDesc;
	QLineEdit* linieStare;
	QLineEdit* liniePro;

	QPushButton* butonadauga;

	QLineEdit* linieCauta;


	vector<Observer*> obs;
	obsStare* obsClosed = new obsStare("closed", srv);
	obsStare* obsProgress = new obsStare("inprogress", srv);
	obsStare* obsOpen = new obsStare("open", srv);

	void connect();

	void update() override;

public:

	MainActivity();
};