#ifndef UML_CLASS_H
#define UML_CLASS_H

#include <QtGui/QMenu>
#include <QtGui/QGraphicsScene>
#include <QtGui/QPainter>
#include "uml_object.h"

class UMLClass : public UMLObject {
public:
	UMLClass();
	virtual QString getName();
	virtual void setName( QString name );
    virtual QRectF boundingRect() const;
    virtual void paint( QPainter * painter, 
						const QStyleOptionGraphicsItem * option, QWidget * widget = 0 );
private:
	QString _name;
	const  QRectF _rect;
};

#endif

