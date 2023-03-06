/****************************************************************************
** Meta object code from reading C++ file 'informationFilmWidget.h'
**
** Created by: The Qt Meta Object Compiler version 68 (Qt 6.3.0)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include <memory>
#include "../../../informationFilmWidget.h"
#include <QtCore/qbytearray.h>
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'informationFilmWidget.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 68
#error "This file was generated using the moc from 6.3.0. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
QT_WARNING_PUSH
QT_WARNING_DISABLE_DEPRECATED
struct qt_meta_stringdata_TextFilmWidget_t {
    const uint offsetsAndSize[10];
    char stringdata0[43];
};
#define QT_MOC_LITERAL(ofs, len) \
    uint(offsetof(qt_meta_stringdata_TextFilmWidget_t, stringdata0) + ofs), len 
static const qt_meta_stringdata_TextFilmWidget_t qt_meta_stringdata_TextFilmWidget = {
    {
QT_MOC_LITERAL(0, 14), // "TextFilmWidget"
QT_MOC_LITERAL(15, 12), // "textIntrodus"
QT_MOC_LITERAL(28, 0), // ""
QT_MOC_LITERAL(29, 8), // "QString*"
QT_MOC_LITERAL(38, 4) // "text"

    },
    "TextFilmWidget\0textIntrodus\0\0QString*\0"
    "text"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_TextFilmWidget[] = {

 // content:
      10,       // revision
       0,       // classname
       0,    0, // classinfo
       1,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       1,       // signalCount

 // signals: name, argc, parameters, tag, flags, initial metatype offsets
       1,    1,   20,    2, 0x06,    1 /* Public */,

 // signals: parameters
    QMetaType::Void, 0x80000000 | 3,    4,

       0        // eod
};

void TextFilmWidget::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        auto *_t = static_cast<TextFilmWidget *>(_o);
        (void)_t;
        switch (_id) {
        case 0: _t->textIntrodus((*reinterpret_cast< std::add_pointer_t<QString*>>(_a[1]))); break;
        default: ;
        }
    } else if (_c == QMetaObject::IndexOfMethod) {
        int *result = reinterpret_cast<int *>(_a[0]);
        {
            using _t = void (TextFilmWidget::*)(QString * );
            if (*reinterpret_cast<_t *>(_a[1]) == static_cast<_t>(&TextFilmWidget::textIntrodus)) {
                *result = 0;
                return;
            }
        }
    }
}

const QMetaObject TextFilmWidget::staticMetaObject = { {
    QMetaObject::SuperData::link<QWidget::staticMetaObject>(),
    qt_meta_stringdata_TextFilmWidget.offsetsAndSize,
    qt_meta_data_TextFilmWidget,
    qt_static_metacall,
    nullptr,
qt_incomplete_metaTypeArray<qt_meta_stringdata_TextFilmWidget_t
, QtPrivate::TypeAndForceComplete<TextFilmWidget, std::true_type>, QtPrivate::TypeAndForceComplete<void, std::false_type>, QtPrivate::TypeAndForceComplete<QString *, std::false_type>



>,
    nullptr
} };


const QMetaObject *TextFilmWidget::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *TextFilmWidget::qt_metacast(const char *_clname)
{
    if (!_clname) return nullptr;
    if (!strcmp(_clname, qt_meta_stringdata_TextFilmWidget.stringdata0))
        return static_cast<void*>(this);
    return QWidget::qt_metacast(_clname);
}

int TextFilmWidget::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 1)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 1;
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 1)
            *reinterpret_cast<QMetaType *>(_a[0]) = QMetaType();
        _id -= 1;
    }
    return _id;
}

// SIGNAL 0
void TextFilmWidget::textIntrodus(QString * _t1)
{
    void *_a[] = { nullptr, const_cast<void*>(reinterpret_cast<const void*>(std::addressof(_t1))) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}
struct qt_meta_stringdata_NumarFilmWidget_t {
    const uint offsetsAndSize[10];
    char stringdata0[42];
};
#define QT_MOC_LITERAL(ofs, len) \
    uint(offsetof(qt_meta_stringdata_NumarFilmWidget_t, stringdata0) + ofs), len 
static const qt_meta_stringdata_NumarFilmWidget_t qt_meta_stringdata_NumarFilmWidget = {
    {
QT_MOC_LITERAL(0, 15), // "NumarFilmWidget"
QT_MOC_LITERAL(16, 13), // "numarIntrodus"
QT_MOC_LITERAL(30, 0), // ""
QT_MOC_LITERAL(31, 4), // "int*"
QT_MOC_LITERAL(36, 5) // "numar"

    },
    "NumarFilmWidget\0numarIntrodus\0\0int*\0"
    "numar"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_NumarFilmWidget[] = {

 // content:
      10,       // revision
       0,       // classname
       0,    0, // classinfo
       1,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       1,       // signalCount

 // signals: name, argc, parameters, tag, flags, initial metatype offsets
       1,    1,   20,    2, 0x06,    1 /* Public */,

 // signals: parameters
    QMetaType::Void, 0x80000000 | 3,    4,

       0        // eod
};

void NumarFilmWidget::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        auto *_t = static_cast<NumarFilmWidget *>(_o);
        (void)_t;
        switch (_id) {
        case 0: _t->numarIntrodus((*reinterpret_cast< std::add_pointer_t<int*>>(_a[1]))); break;
        default: ;
        }
    } else if (_c == QMetaObject::IndexOfMethod) {
        int *result = reinterpret_cast<int *>(_a[0]);
        {
            using _t = void (NumarFilmWidget::*)(int * );
            if (*reinterpret_cast<_t *>(_a[1]) == static_cast<_t>(&NumarFilmWidget::numarIntrodus)) {
                *result = 0;
                return;
            }
        }
    }
}

const QMetaObject NumarFilmWidget::staticMetaObject = { {
    QMetaObject::SuperData::link<QWidget::staticMetaObject>(),
    qt_meta_stringdata_NumarFilmWidget.offsetsAndSize,
    qt_meta_data_NumarFilmWidget,
    qt_static_metacall,
    nullptr,
qt_incomplete_metaTypeArray<qt_meta_stringdata_NumarFilmWidget_t
, QtPrivate::TypeAndForceComplete<NumarFilmWidget, std::true_type>, QtPrivate::TypeAndForceComplete<void, std::false_type>, QtPrivate::TypeAndForceComplete<int *, std::false_type>



>,
    nullptr
} };


const QMetaObject *NumarFilmWidget::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *NumarFilmWidget::qt_metacast(const char *_clname)
{
    if (!_clname) return nullptr;
    if (!strcmp(_clname, qt_meta_stringdata_NumarFilmWidget.stringdata0))
        return static_cast<void*>(this);
    return QWidget::qt_metacast(_clname);
}

int NumarFilmWidget::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 1)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 1;
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 1)
            *reinterpret_cast<QMetaType *>(_a[0]) = QMetaType();
        _id -= 1;
    }
    return _id;
}

// SIGNAL 0
void NumarFilmWidget::numarIntrodus(int * _t1)
{
    void *_a[] = { nullptr, const_cast<void*>(reinterpret_cast<const void*>(std::addressof(_t1))) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}
QT_WARNING_POP
QT_END_MOC_NAMESPACE
