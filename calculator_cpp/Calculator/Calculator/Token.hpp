#include<string>
#include<ostream>
class Token {
private:
	std::string type;
	std::string value;

public:
	Token() {};
	~Token() {};
	friend std::ostream& operator<< (std::ostream& os, Token& token); 
};

class Add: private Token {

};