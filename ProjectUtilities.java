
public class ProjectUtilities {

    public static boolean isNumeric(String inputData) {
        return inputData.matches("\\d+(\\d+)?");
    }

    public static final String documentTypes[] = { "Cédula de ciudadanía", "Tarjeta de identidad", "Cédula de extranjería", "Pasaporte"};
    public static final String clientTypes[] = { "Natural", "Corporativo"};

    public static short convertDocumentType(String documentType){
        if(documentTypes[0].equals(documentType)){
            return (short) 0;
        } else if (documentTypes[1].equals(documentType)){
            return (short) 1;
        } else if (documentTypes[2].equals(documentType)){
            return (short) 2;
        } else if (documentTypes[3].equals(documentType)){
            return (short) 3;
        }
        return (short) -1;
    }

    public static String clearWhiteSpaces(String inputString) {
        StringBuilder newString = new StringBuilder();
        boolean whiteSpace = false;
        boolean firstTime = true;
        for (int i = 0; i < inputString.length(); i++) {
            if (firstTime) {
                if (!(inputString.charAt(i) == ' ')) {
                    firstTime = false;
                    newString.append(inputString.charAt(i));
                }
            } else if (!(inputString.charAt(i) == ' ')) {
                if (whiteSpace) {
                    newString.append(" ").append(inputString.charAt(i));
                } else {
                    newString.append(inputString.charAt(i));
                }
                whiteSpace = false;
            } else {
                if (!whiteSpace) {
                    whiteSpace = true;
                }
            }
        }
        return newString.toString();
    }

    public static String convertDocumentTypeString(short documentType){
        if(documentType == (short) 0){
            return documentTypes[0];
        } else if (documentType == (short) 1){
            return documentTypes[1];
        } else if (documentType == (short) 2){
            return documentTypes[2];
        } else if (documentType == (short) 3){
            return documentTypes[3];
        }
        return documentTypes[0];
    }

    public static short convertClientType(String type){
        if (clientTypes[0].equals(type)){
            return (short) 0;
        } else if (clientTypes[1].equals(type)){
            return (short) 1;
        }
        return (short) -1;
    }

    public static String convertClientTypeString(short type){
        if (type == (short) 0){
            return clientTypes[0];
        } else if (type == (short) 1){
            return clientTypes[1];
        }
        return clientTypes[0];
    }

}
