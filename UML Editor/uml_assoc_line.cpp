#include "uml_assoc_line.h"
#include "uml_object.h"

UMLAssociationLine::UMLAssociationLine()
{

}

UMLAssociationLine::UMLAssociationLine( UMLObjectPort * startPort, UMLObjectPort * endPort )
	: UMLLink( startPort, endPort )
{

}
