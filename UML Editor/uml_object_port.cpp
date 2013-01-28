#include "uml_object_port.h"
#include "uml_object.h"
#include "uml_link.h"

UMLObjectPort::UMLObjectPort()
	: _umlLinkList()
	, _offset( QPointF() )
	, _parentUMLObj( NULL )
{

}

QPointF UMLObjectPort::getOffset()
{
	return _offset;
}

void UMLObjectPort::setOffset( QPointF offset )
{
	_offset = offset;
}

UMLObject * UMLObjectPort::getParentUMLObj()
{
	return _parentUMLObj;
}

void UMLObjectPort::setParentUMLObj( UMLObject * parentUMLObj )
{
	_parentUMLObj = parentUMLObj;
}

void UMLObjectPort::addLink( UMLLink * link )
{
	_umlLinkList.append( link );
}

void UMLObjectPort::updateLinks()
{
	QListIterator< UMLLink * > it( _umlLinkList );
	while( it.hasNext() ) {
		it.next()->drawLink();
	}
}
