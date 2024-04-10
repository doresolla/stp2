/*  
     /\\\\\\\\\\\    /\\\\\\\\\\\\\\\  /\\\\\\\\\\\  /\\\\\\\\\\\\\\\  /\\\        /\\\     /\\\\\\\\\     /\\\\\     /\\\         
    /\\\/////////\\\ \/\\\///////////  \/////\\\///  \///////\\\/////  \/\\\       \/\\\   /\\\\\\\\\\\\\  \/\\\\\\   \/\\\        
    \//\\\      \///  \/\\\                 \/\\\           \/\\\       \/\\\       \/\\\  /\\\/////////\\\ \/\\\/\\\  \/\\\       
      \////\\\         \/\\\\\\\\\\\         \/\\\           \/\\\       \/\\\\\\\\\\\\\\\ \/\\\       \/\\\ \/\\\//\\\ \/\\\      
          \////\\\      \/\\\///////          \/\\\           \/\\\       \/\\\/////////\\\ \/\\\\\\\\\\\\\\\ \/\\\\//\\\\/\\\     
              \////\\\   \/\\\                 \/\\\           \/\\\       \/\\\       \/\\\ \/\\\/////////\\\ \/\\\ \//\\\/\\\    
        /\\\      \//\\\  \/\\\                 \/\\\           \/\\\       \/\\\       \/\\\ \/\\\       \/\\\ \/\\\  \//\\\\\\   
        \///\\\\\\\\\\\/   \/\\\\\\\\\\\\\\\  /\\\\\\\\\\\       \/\\\       \/\\\       \/\\\ \/\\\       \/\\\ \/\\\   \//\\\\\  
           \///////////     \///////////////  \///////////        \///        \///        \///  \///        \///  \///     \/////  
*/  
//                                   ______________________________________________
                                    //--------CREATED BY SEITANIS THANASIS--------\\
                                   //---------------VERSION 1/9/2019---------------\\
                                  //---------Tutorial on how to write code----------\\
                                 //-----------with Nextion and Arduino---------------\\
                                //----------------DEMO with  2 Buttons----------------\\
                               //--------------------www.seithan.com-------------------\\
                              //---Many Thanks to SERASIDIS VASSILIS for his guidance---\\ 
                             //--------------------www.serasidis.gr----------------------\\
                              
#define ARRAY_ROWS 20 // Number of rows. Each row will be sented as String values at the variables created at Nextion(from va0 to va19)
#define ROW_LENGTH 38 // Max number of characters in a single row. At the Nextion, the txt_maxl is setted to 40. 
                      /* The text frame,however, can hold 37 characters with the font we have choosed
                       * So the 38 ROW_LENGTH is for 37 characters and the last is for null terminator
                       */
#define Nextion Serial
                      // define that Serial will de writed as Nextion (when we write Nextion.print the  compiler read Serial.print)    



const char text [ARRAY_ROWS] [ROW_LENGTH]{
  {"1) Test text of 37 characters @@@@123"},
  {"2) This the page of the demonstration"},
  {"3) For The scrolling TEXT on NEXTION_"},
  {"4) Crtd by SEITANIS THANASIS 8-8-2019"},
  {"5) Find it at my site www.seithan.com"},
  {"6) Thanks to the people that helped _"},
  {"7) And supporting me with things that"},
  {"8) I am dealing with................."},
  {"9) This project helps to understand__"},
  {"10) Logic of Nextion displays and how"},
  {"11) Can take all the capabilities of_"},
  {"12) Same time you can see how Buttons"},
  {"13) Text box, variables can be used**"},
  {"14) There is many ways for having the"},
  {"15) Same result in programming ......"},
  {"16) The reason that you must choose__"},
  {"17) NEXTION low need coding for the__"},
  {"18) graphic display can do everything"},
  {"19) Just tell it to do it ..........."},
  {"20) This is the END of TEXT LINE20__"},
};


    /* above we have create a character array of 20 place (ARRAY_ROWS) and each place has max 38 characters (ROW_LENGTH)
     * Take in mind that in ARRAY we Have 20 places but the first place is number 0 and last is 19
     * when we want to call the first place we write text[0] and for 20th place text[19]
     */ 
void setup(){

 
  
  Nextion.begin(115200); // starting the serial port at 115200. NEXTION MUST HAVE THE SAME RATE. For this we write at
                         //  first page to the preinitialize event the command @  baud=115200  @ 
                         // NOTE "bauds" will change the default baud rate off 9600 untill it changed again
  delay(500);
}


void loop(){

  Nextion_serial_listen();

}


void Nextion_serial_listen() 

/* The communication between the Nextion Displays and the Arduino is quite simple. There is no need for libraries and complicate commands
 * as Nextion uses a simple and complete instruction_set. A library cannot cover and guess all the needs of the projects wide field.
 * The only that we must do is to organize the commands that we are going to use with a simply as possible method,
 * identifying them with Arduino code and assign them to the functions we want.
 * Nextion has a protocol to send the ids of the components via Serial but is very difficult 
 * For this we develop our own protocol to categorize, organize, read, and assign the commands.
 * In here we want to thank again Vassilis Serasidis for his advises on this.
 * We use the following Data Format: <#> <len> <cmd> <id>
 * It is more practical to send a specific command that it will activate the function we want.
 * As you can send the same command from different events and pages as times you want.
 * So we have to organize the commands that we are going to use and assign them to the functions
 * AND NOT trying to assign the IDs from a big number of objects to the function, as Nextion do with the event id protocol.
 * An example with the group command “Page” is :
 * From nextion we send in HEX the following:< printh 23 02 50 00 >
 *           means < # 2 P 0 >  
 *           <#>   declares that a command is followed
 *           <len> declares the number of bytes that will follow (len = 2, <P> is the one Byte and <0> is the other)
 *           <cmd> declares the group of commands( P = page L=Line)
 *           <id>  declares the id, to which the command is referring to. ( id = 0)
 * This means that we are in page 0 (we have write in everys page "preinitialize page" the command
 * printh 23 02 50 xx (where xx is the page id in HEX, 00 for 0, 01 for 1, etc).
 * We have assign a fuction name < first_refresh_page(uint8_t page) >
 * and with a switch case command, we sent to each page the data that must updated in first load.
 * A second example  is this with the “Line” command group (<#> <len> <cmd> <nextion_var> <cnt>)
 * From nextion we send in HEX the following:< printh 23 03 4C 04 01 >
 *           means < # 3 L 4 1 >  
 *           <#>   declares that a command is followed
 *           <len> declares the number of bytes that will follow (len = 3, <L> is the one Byte <4> and <1>is the other)
 *           <cmd> declares the group of commands( P = page L=Line)
 *           <nextion_var>  is the Number of the variable on Nextion that we want to write
 *           <cnt> is the number of the text array Line that we want to store into Nextion's variable
 * We have assign the function < sending_text(byte nextion_var, byte cnt) >
 * where we sent the last 2 bytes to local variables of the function.
*/

 /* We have write in everys page "preinitialize page" the command
  *  printh 23 02 50 xx (where xx is the page id in HEX, 00 for 0, 01 for 1, etc).
  * <<<<Every event written on Nextion's pages preinitialize page event will run every time the page is Loaded>>>>
  *  it is importand to let the Arduino "Know" when and which Page change.
  */
{
    if(Nextion.available() > 2){                // Read if more then 2 bytes come (we always send more than 2 <#> <len> <cmd> <id>
        char start_char = Nextion.read();      // Create a local variable (start_char) read and store the first byte on it  
        if(start_char == '#'){                // And when we find the character #
          uint8_t len = Nextion.read();      // Create local variable (len) / read and store the value of the second byte
                                            // <len> is the lenght (number of bytes following) 
          unsigned long tmr_1 = millis();
          boolean cmd_found = true;
            
          while(Nextion.available() < len){ // Waiting for all the bytes that we declare with <len> to arrive              
            if((millis() - tmr_1) > 100){    // Waiting... But not forever...... 
              cmd_found = false;              // tmr_1 a timer to avoid the stack in the while loop if there is not any bytes on Serial
              break;                            
            }                                     
            delay(1);                            // Delay for nothing delete it if you want
          }                                   
                                               
            if(cmd_found == true){            // So..., A command is found (bytes in Serial buffer egual more than len)
              uint8_t cmd = Nextion.read();  // Create local variable (cmd). Read and store the next byte. This is the command group
                                             
              switch (cmd){
                                    
                case 'P': /*or <case 0x50:>  IF 'P' matches, we have the command group "Page". 
                           *The next byte is the page <Id> according to our protocol.
                           *It reads the next byte as a type of <uint8_t variable> and direct send it to:
                           *first_refresh_page() function as parameter. 
                           */
                  first_refresh_page((uint8_t)Nextion.read());  
                  break;
                
                case 'L': 
                /* < case 0x4C: > (0x4c = ‘L’) IF there is a matching with 'L' then we have the command group "Line" according to the protocol
                 * We are waiting 2 more bytes from the <nextion_var> and the <cnt>... (<#> <len> <cmd> <nextion_var> <cnt>)
                 * <nextion_var> is the Number of the variable on Nextion that we want to write
                 * <cnt> is the number of the text array Line that we want to store into Nextion's variable
                 * From Nextion we have sent < printh 23 04 4C 04 xx > where 04, in this example, the <nextion_var> and xx the <cnt>
                 * Same as the above, we are going to read the next 2 bytes as <uint8_t> and direct send them to
                 * < sending_text () > fuction as parameters to the local variables of the function 
                 * < sending_text(byte nextion_var, byte cnt) > that will be created on start up of the Function
                 */

                    sending_text((uint8_t)Nextion.read(),(uint8_t)Nextion.read()); 
                    
                break;
                
              }
            }
        }  
    }    
}

void first_refresh_page(uint8_t page)   
{
        /* a local variable uint8_t <page> it will be created and the value that it gets is the one
         * that we have send from <case 'P'>. Every page send it's Id (we have write in everys page "preinitialize page"
         * the command <printh 23 02 50 xx> (where xx is the page id in HEX, 00 for 0, 01 for 1, etc)
         * we are going to identifying the Id of the page and we are going to send all the values
         * that need refresh from the Arduino  
         */
    switch(page){
    
    
        case 0x00: 
        
            // Home page. Nothing to refresh for now
                        
        break;
        
        
        case 0x01: 
                       
            Nextion.print("array_rows.val="); // Sending to Nextion the value of ARRAY_ROWS, that represent how many Lines is the array
            Nextion.print(ARRAY_ROWS);       // We update the value of array_rows variable on Nextion, to be equal to ARRAY_ 
            NextionEndCommand(); 
            
            Nextion.print("n1.val=array_rows.val"); //Set the value of n1 numeric component,  
            NextionEndCommand();                   // to be equal to the value of array_rows variables on Nextion
            
           /* Nextion.print("t1.txt=\"");              
            Nextion.println("Welcome to message page"); //We send & print a message to the t1 textbox
            Nextion.print("there are ");               // 
            Nextion.print(ARRAY_ROWS);                // 
            Nextion.println(" NEW Message");         //
            Nextion.print("TOUCH HERE TO READ");
            Nextion.print("\"");
            NextionEndCommand(); 
           */
            
            /* as you can see, the < print > and <println> are working fine to change into new line,
             * because < println > sends a carriage return character (ASCII 13, or '\r')
             * and a newline character (ASCII 10, or '\n') at the end of the text.
             * We get the same message result with the following.
            */
            Nextion.print("t1.txt=\"Welcome to message page\\rthere are "); 
            Nextion.print(ARRAY_ROWS);
            Nextion.print(" NEW Messages\\rTOUCH HERE TO READ\"");
            NextionEndCommand();
            /* In Nextion with the < \r > creates 2 bytes 0x0D 0x0A the first is the return character (ASCII 13, or '\r')
             * and the second is the newline character (ASCII 10, or '\n'). 
             *We must send to nextion the 2 bytes < \r > of the backslash (\) and character (r) 
             *to send the backslash char (\) we must add a (\) in front, for the escape character, due to we want to print the second (\) as character
             */
             
             /*When we are at Nextion we must include the < \r > inside " ", result: < "\r" >. 
              * When it is inside the text there is no need for extra "". The quote marks of the text are enough 
              * Example 1 < t1.txt="LINE1\rLINE2" >
              * Example 2 < t1.txt="LINE1\rLINE2"+"\r"+t3.txt> we add a new Line with the text of t3 component
              */
            sending_text(0,0);           // to fuction <sending_text (byte nextion_var, byte cnt)> send 2 values one for each variable
            sending_text(1,1);          // this is for sending and store the first 4 Lines on Nextions 4 first variables 
            sending_text(2,2);         // Nextion variables va0, va1, va2, va3
            sending_text(3,3);        // <sending_text(3,3);> this is going to store to the va3 on Nextion the array line 3 
            
            Nextion.print("cnt.val=3");   //After first 4 lines printed on Nextion, the <cnt> value is 3 and we update that on Nextion  
            NextionEndCommand();         // Remember that 4th line or place called with the number 3 (0,1,2,3,...19)
        break;
        /* For every component on Nextion, we can chance only attributes that are showed with GREEN color when Nextion is “running”
         *with the following prototype <component name>.<attribute>=<value> . If attribute is text, the value must be inside <" "> quotes.
         *Supposing we want to send to component t3 the text < HELLO >  the command for Nextion is < t3.txt="HELLO" >,
         *from Arduino, we write < Serial.print("t3.txt=\"HELLO\"") >; 
         *When we type <\"> means that <"> is a character and it's going to print it over serial as character. Else <"> is a STRUCTURE.
         *To change the font color t3, we write on Nextion: < t3.pco=BLUE > or < t3.pco=<31> >
         *From Arduino:< Serial.print(“t3.pco=BLUE”); > or < Serial.print(“t3.pco=31”); >\
         *TIP: The exact names of the attributes and the values they can get, can be found on Nextion Editor at the attribute menu.
         */
         
         
         case 0x02:
            
            Nextion.print("array_rows.val="); // Sending to Nextion the value of ARRAY_ROWS, that represent how many Lines is the array
            Nextion.print(ARRAY_ROWS);          
            NextionEndCommand(); 
            
            Nextion.print("n0.val="); // Sending to Nextion the value of n0.val a numeric component that is hidden and used as variable on nextions code
            Nextion.print(ARRAY_ROWS - 3);          
            NextionEndCommand(); 
            
            Nextion.print("h0.maxval="); // setting the max value that Slider on nextion can take
            Nextion.print(ARRAY_ROWS - 4);          
            NextionEndCommand();
            
            Nextion.print("h0.val=h0.maxval"); //  setting the value of the slider to its max value ( pointer on the top)    
            NextionEndCommand();            
            
            save_text_to_variables_example2();
            
            break;
            
            
            case 0x03:
             
                        
            Nextion.print("n1.val="); // Sending to Nextion the value of ARRAY_ROWS, that represent how many Lines the array is
            Nextion.print(ARRAY_ROWS);       // We update the value of n1 numeric component on Nextion, to be equal to ARRAY_ROWS 
            NextionEndCommand(); 
             
            send_text_to_g1();
                       
            break;
            
    }
}       



void sending_text(byte nextion_var, byte cnt){
        /*From the Function < Nextion_serial_listen () > and <case 'L'> we have send the 2 bytes <nextion_var> and <cnt>
         * directly to this Function and run the fuction with the command <sending_text((uint8_t)Nextion.read(),(uint8_t)Nextion.read());> .
         * In this fuction we create 2 Byte local variables  and we assign the read bytes from case 'L' as their values 
         */
         
        /* From Nextion each time we press the <up> or <down> scrolling button we raise the variable on Nextion <cnt> by one cnt++ or dicrease it by one cnt--
         *In this function we want to store the line from the text array that we ask with <cnt> to the variable va4 on Nextion
         * va4 is for this examble we can write on every var that we will sent throw the <nextion_var>
         * the command for nextion : va4.txt="text from the <cnt> Line of char text text[cnt]"
         */

    if(cnt < ARRAY_ROWS){
    
         Nextion.print("va");
         Nextion.print(nextion_var);
         Nextion.print(".txt=\"");
         Nextion.print(text[cnt]);
         Nextion.print("\"");
         NextionEndCommand();
         
        // This is also working Un-comment those 4 lines and comment the above 6 lines
        
        /* char buffer[50] ; 
        
        sprintf(buffer, "va%u.txt=\"%s\"", nextion_var, text[cnt]);
        Nextion.print(buffer);
         NextionEndCommand();
         */
        
        
    }
}

void save_text_to_variables_example2(){
  
  char temp_data[50];
  
  for(int i = 0; i < ARRAY_ROWS; i++){
    sprintf(temp_data, "va%u.txt=\"%s\"", i, text[i]);
    Nextion.print(temp_data);
    NextionEndCommand();
    delay(10);                           // Give some time to Nextion to read from Serial Buffer and Avoid buffer overflow
    
    /* A very useful command is the <sprintf>. This command will construct a string from different type of variables and will store it
         * in a char array in our case <buffer>. We must declare the length of the array to be long enough to hold all the characters.
         * With sprintf we can include variables within a text using format specifiers with the prefix < % >, format specifiers are replaced  
         * by the values specified in subsequent additional arguments. 
         * sprint(char array to store value, "<TEXT TO PRINT %[format specifier]NEXT TEXT%[format specifier]",<name of 1 variable>,<name of 2 variable>)
         * %d = signed integer               %f = floating point number                  }
         * %s = string                     %.1f = float to 1 decimal place               } 
         * %c = character                  %.3f = float to 3 decimal places              } Format specifier table from
         * %e = scientific notation          %g = shortest representation of %e or %f    } https://arduinobasics.blogspot.com/2019/05/sprintf-function.html            
         * %u = unsigned integer             %o = unsigned octal                         }
         * %x = unsigned hex (lowercase)     %X = unsigned hex (uppercase)               }
         * %hd = short int                  %ld = long int                               }
         * %lld = long long int                                                          }
         *   More for sprintf-function to: http://www.cplusplus.com/reference/cstdio/sprintf/
         */
  }
}

void send_text_to_g1()
{
 char temp_data[50];
  
  for(int i = 0; i < ARRAY_ROWS; i++){
    sprintf(temp_data, "g1.txt+=\"%s\\r\"", text[i]);
    Nextion.print(temp_data);
    NextionEndCommand();
  }
}

void NextionEndCommand()
{
    // with this two ways we can sent the 3 bytes so Nextion can understand the end of a command. Choose freerly.
    
  /* Nextion.write(0xff);
     Nextion.write(0xff);
     Nextion.write(0xff); */
  
    Nextion.print("\xFF\xFF\xFF");
  
 
}
