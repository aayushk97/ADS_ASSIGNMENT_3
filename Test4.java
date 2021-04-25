class Test4{
	public static void main(String[] args){
		String msg = "This is me";
		System.out.println("Print: " + Crypto.sha256(msg));
	}

	public static String toBitString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
            sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }
}