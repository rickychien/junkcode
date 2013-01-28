#ifndef UML_USECASE_H
#define UML_USECASE_H

#include <QtGui/QMenu>
#include <QtGui/QGraphicsScene>
#include <QtGui/QPainter>
#include "uml_object.h"

class UMLUseCase : public UMLObject {
public:
	UMLUseCase();
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
