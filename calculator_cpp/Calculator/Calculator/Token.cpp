#include "Token.hpp"
#include <iomanip>
std::ostream& operator<<(std::ostream& os, Token& token)
{
	os << "< " << std::setw(8) << std::left << token.type
		<< ", " << token.value << " >" << std::endl;
	return os;
}
