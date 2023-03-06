#include "mainActivity.h"
//#include <iostream>
//#include "repository.h"
#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    
    MainActivity* m = new MainActivity;
    m->show();
    //RepoMelodii r{ "fisierDate.txt" };
    //for (auto m : r.getAll())
    //{
    //    cout << m.toString() << endl;
    //}
  
    return a.exec();
}
