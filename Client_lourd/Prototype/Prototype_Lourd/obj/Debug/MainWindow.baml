   M S B A M L   `   `   `  ����J  FPrototype_Lourd, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null1clr-namespace:Prototype_LourdPrototype_Lourd  5   	        Prototype_Lourd.MainWindow   5   	   W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   /localclr-namespace:Prototype_Lourd     5   	     � Title$  
MainWindow��5   	   $	��450��6   $	��800��6)   ���	#FF979BDB.��5
      �  � ColumnDefinitions 5   
   �� 5      $	��93* �6   �� 5      $
��106* �6   .+�5   
   �� -   5         ��Name$ connectToIPButton��5      $��	Connexion��6,   $
��Left=�6@   $��
248,99,0,0q�6[   $	��Top=�6o   $��75��6�   �� 5   
   -   5         ��Name$ 	ipTextBox��5      $��	127.0.0.1��6%   $
��Left=�66   $��20��6Q   $��
116,99,0,0q�6]    �� TextWrapping$
 Wrap=�6q   $	��Top=�6�   $	��120��6�   6�   �� 5   
   -   5         ��Name$ 	ipAddress��5      $
��Left=�6'   $��	50,99,0,0q�6B   $
��Wrap=�6U   $��Adresse IP:��6i   $	��Top=�6|   $��20��6�   $��63��6�   
���   �6�   �� 5   
   -   5         ��Name$ messageList��5   >   $
��Left=�6S   $	��300��6n   $��32.4,43,0,0q�6{   $	��Top=�6�   $	��364��6�   $��1��6�   {�6   ��  �� Path$ HasValidUsername�6�   �� 5   
   -   5        $ 
messageBox��5   >   $
��Left=�6R   $��30��6m   $��42.4,348,0,0q�6y   $
 Wrap=�6�   $	��Top=�6�   $	��260��6�   $��1��6	  {�6   �� $ HasValidUsername�6	  �� 5   
   -   5        $ sendMessageButton��5   =   $��Envoyer��6X   $
��Left=�6j   $��307.4,348,0,0q�6�   $	��Top=�6�   $��89��6�   $��30��6�   $��1��6�   {�6   �� $ HasValidUsername�6�   �� 5   
   -   5         ��Name$
 logo��5      $
��Left=�6   $��45��69   $��	24,10,0,0q�6E   $	��Top=�6X   $	��199��6p   $��PolyMessagerie.png��6|   �� 5   
   -   5        $ usernameTextBlock��5      $
��Left=�6/   $��
37,202,0,0q�6J   $
��Wrap=�6^   $��Pseudonyme:��6r   $	��Top=�6�   �� 5   
   -	   5        $ usernameTextBox��5   e   $
��Left=�6~   $��23��6�   $��116,194,0,0q�6�   $
 Wrap=�6�   $	��Top=�6�   $	��120��6�   {�6   �� $ IsConnected���69   �� $ Username�	 �� Mode$	 TwoWay=�6�   �� 5   
   -
   5        $ selectUsernameButton��5   8   $��Ok��6V   $
��Left=�6c   $��248,195,0,0q�6~   $	��Top=�6�   $��75��6�   {�6   �� $ IsConnected�6�   �� 5   
   -   5        $ disconnectButton��5   8   $��Deconnexion��6R   $
��Left=�6h   $��248,124,0,0q�6�   $	��Top=�6�   $��75��6�   {�6   �� $ IsConnected�6�   5      5      