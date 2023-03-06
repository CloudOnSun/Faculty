#include "mainActivity.h"
#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainActivity* m = new MainActivity;
    m->show();
    return a.exec();
}
