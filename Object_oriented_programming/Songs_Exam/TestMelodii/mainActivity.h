#pragma once
#include <qwidget.h>
#include <qtabwidget.h>
#include <qtableview.h>
#include <QAbstractTableModel>
#include "service.h"
#include <qpushbutton.h>
#include <qlineedit.h>


class MyTableModel : public QAbstractTableModel
{
	vector <Melodie> melodii;

public:

	MyTableModel(vector <Melodie> melodii): melodii{melodii} {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return (int)melodii.size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return 5;
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override
	{
		int row = index.row();
		int col = index.column();

		if (role == Qt::DisplayRole)
		{
			Melodie m = melodii.at(row);
			if (col == 0)
			{
				return QString::number(m.getId());
			}
			if (col == 1)
			{
				return QString::fromStdString(m.getTitlu());
			}
			if (col == 2)
			{
				return QString::fromStdString(m.getArtist());
			}
			if (col == 3)
			{
				return QString::number(m.getRank());
			}
			if (col == 4)
			{
				int nr = 0;
				int r = m.getRank();
				for (auto mel : melodii)
				{
					if (mel.getRank() == r)
					{
						nr++;
					}
				}
				return QString::number(nr);
			}
		}
		return QVariant{};
	}

	void set(const vector<Melodie> melodii)
	{
		this->melodii = melodii;
		auto topLeft = createIndex(0, 0);
		auto bottomRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, bottomRight);
		emit layoutChanged();
	}

	Qt::ItemFlags flags(const QModelIndex& ) const
	{
		return Qt::ItemIsSelectable | Qt::ItemIsEditable | Qt::ItemIsEnabled;
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role) const
	{
		if (role == Qt::DisplayRole)
		{
			if (orientation == Qt::Horizontal)
			{
				switch (section)
				{
				case 0:
					return tr("ID");
				case 1:
					return tr("Titlu");
				case 2:
					return tr("Artist");
				case 3:
					return tr("Rank");
				case 4:
					return tr("Acelasi rank");
				default:
					return QString("");
				}
			}
		}
		return QVariant();
	}

};



class MainActivity : public QWidget
{
	Q_OBJECT

private:

	RepoMelodii repo{ "fisierDate.txt" };
	SrvMelodii srv{ repo };
	QTableView* tableView;
	MyTableModel* tableModel;

	QLineEdit* titluEdit;
	QSlider* slider;
	QPushButton* butonUpdate;
	QPushButton* butonSterge;

	void connect();
	void update();
	void itemSelectat();
	void sterge();
	void paintEvent(QPaintEvent* ev) override;

public:
	
	MainActivity(QWidget* parent = nullptr);
};

